package com.xet.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xet.R
import com.xet.data.Result
import com.xet.domain.model.User
import com.xet.domain.usecase.friend.FriendUseCases
import com.xet.domain.usecase.user.UserUseCases
import com.xet.presentation.search.UpdateInviteResult
import com.xet.presentation.signup.SignUpFormState
import kotlinx.coroutines.launch

class ProfileViewModel(
   private val userUseCases: UserUseCases,
   private val friendUseCases: FriendUseCases
): ViewModel() {

   private lateinit var user: User

   private val initForm = false

   private val _invitesResult = MutableLiveData<InvitesResult>()
   val invites: LiveData<InvitesResult> = _invitesResult

   private val _updateProfileResult = MutableLiveData<Boolean>()
   val updateProfile: LiveData<Boolean> = _updateProfileResult

   private val _updateInviteResult = MutableLiveData<UpdateInviteResult>()
   val updateInvite: LiveData<UpdateInviteResult> = _updateInviteResult

   private val _profileForm = MutableLiveData<ProfileFormState>()
   val profileFormState: LiveData<ProfileFormState> = _profileForm

   fun loadUser(): ProfileResult {
      val result = userUseCases.loggedInUser()

      return if (result is Result.Success) {
         user = result.data
         ProfileResult(success = result.data)
      } else {
         ProfileResult(error = R.string.profile_load_error)
      }
   }

   fun updateProfile(fullName: String, username: String) {
      viewModelScope.launch {
         val result = userUseCases.doUpdateProfile(fullName, username)

         _updateProfileResult.value = result is Result.Success
      }
   }

   fun loadInvites() {
      viewModelScope.launch {
         val result = friendUseCases.getInvites(user.userId)

         if (result is Result.Success) {
            if (result.data.isEmpty()) {
               _invitesResult.value = InvitesResult(empty = R.string.profile_invites_empty)
            } else {
               _invitesResult.value = InvitesResult(success = result.data)
            }
         } else if (result is Result.Error) {
            _invitesResult.value = InvitesResult(error = R.string.profile_invites_error)
         }
      }
   }

   fun updateInvite(userFrom: String, accepted: Boolean) {
      viewModelScope.launch {
         val result = friendUseCases.updateInvite(userFrom, user.userId, accepted)

         if (result is Result.Success) {
            if (accepted) {
               _updateInviteResult.value = UpdateInviteResult(success = R.string.profile_invite_update_accepted)
            } else {
               _updateInviteResult.value = UpdateInviteResult(success = R.string.profile_invite_update_refused)
            }
         } else {
            _updateInviteResult.value = UpdateInviteResult(error = R.string.profile_invite_update_fail)
         }

         val userInvites = _invitesResult.value?.success

         if (userInvites != null) {
            for (invite in userInvites) {
               if (invite.userId == userFrom) {
                  userInvites.drop(userInvites.indexOf(invite))
               }
            }
         }

         if (userInvites != null) {
            if (userInvites.isEmpty()) {
                 _invitesResult.value = InvitesResult(empty = R.string.profile_invites_empty)
            } else {
                 _invitesResult.value = InvitesResult(success = userInvites)
            }
         }
      }
   }

   fun formChanged(fullName: String, username: String) {
      val isFullNameValid = fullName.isNotBlank() && fullName != user.displayName
      val isUserNameValid = username.isNotBlank() && username != user.username
      _profileForm.value = ProfileFormState(
         usernameError =  if (!isUserNameValid) R.string.signup_invalid_username else null,
         fullNameError = if (!isFullNameValid) R.string.signup_invalid_full_name else null,
         isDataValid = isFullNameValid && isUserNameValid
      )
   }
}
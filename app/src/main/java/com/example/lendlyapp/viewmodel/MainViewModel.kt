package com.example.lendlyapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lendlyapp.data.local.room.UserDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    userDao: UserDao,
) : ViewModel() {

    val avatarUrl: StateFlow<String?> = userDao.observeCurrentUser()
        .map { it?.avatar }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
}

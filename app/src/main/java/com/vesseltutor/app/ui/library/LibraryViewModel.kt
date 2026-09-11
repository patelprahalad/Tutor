package com.vesseltutor.app.ui.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vesseltutor.app.data.local.entity.ScenarioEntity
import com.vesseltutor.app.data.repository.ScenarioRepository
import com.vesseltutor.app.data.seed.ScenarioCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

class LibraryViewModel(private val repository: ScenarioRepository) : ViewModel() {

    val categories = ScenarioCategory.ALL

    private val _selectedCategory = MutableStateFlow(categories.first())
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val scenarios: StateFlow<List<ScenarioEntity>> = _selectedCategory
        .flatMapLatest { category -> repository.getByCategory(category) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }
}

package otus.homework.flowcats

import androidx.lifecycle.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class CatsViewModel(
    private val catsRepository: CatsRepository
) : ViewModel() {

    private val _catsFlow = MutableStateFlow<Result<Fact>>(Result.Init)
    val catsFlow: StateFlow<Result<Fact>> = _catsFlow.asStateFlow()

    init {
        catsRepository.listenForCatFacts().onEach {
            _catsFlow.value = Result.Success(it)
        }.catch {
            _catsFlow.value = Result.Error(it.localizedMessage)
            it.printStackTrace()
        }.launchIn(viewModelScope)
    }
}

class CatsViewModelFactory(private val catsRepository: CatsRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        CatsViewModel(catsRepository) as T
}
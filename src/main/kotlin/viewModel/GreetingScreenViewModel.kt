package viewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import view.DataSystems

class GreetingScreenViewModel() {
    private var _dataSystem = mutableStateOf<DataSystems?>(null)
    var dataSystem: DataSystems?
        get() = _dataSystem.value
        set(value) {
            _dataSystem.value = value
        }

    private var _showErrorDialog = mutableStateOf(false)
    var showErrorDialog: Boolean
        get() = _showErrorDialog.value
        set(value) {
            _showErrorDialog.value = value
        }

    private var _errorMessage = mutableStateOf("")
    var errorMessage: String
        get() = _errorMessage.value
        set(value) {
            _errorMessage.value = value
        }
}

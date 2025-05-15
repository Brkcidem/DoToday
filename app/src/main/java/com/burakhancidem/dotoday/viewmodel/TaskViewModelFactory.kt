//ViewModel Üretim Sınıfı (Factory)
//Normalde ViewModel'e parametre veremezsin (ViewModel() parametresiz olmalı).
//Bu sınıf sayesinde TaskRepository gibi bağımlılıkları ViewModel'e güvenli ve doğru şekilde geçirebilirsin.
//Ayrıca SavedStateHandle gibi yaşam döngüsüyle uyumlu yapıların aktarımı da sağlanır.

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.burakhancidem.dotoday.repository.TaskRepository
import com.burakhancidem.dotoday.viewmodel.TaskViewModel

class TaskViewModelFactory(
    private val repository: TaskRepository,//ViewModel'in kullanacağı veri kaynağı.
    owner: SavedStateRegistryOwner//Fragment ya da Activity'nin yaşam döngüsüne bağlı SavedState erişimi için gerekli.
) : AbstractSavedStateViewModelFactory(owner, null) {


    //Android ViewModel sağlayıcısı bu fonksiyonu çağırır ve ViewModel'in hangi sınıf olduğunu belirtir.
    // Biz de uygun sınıfı üretmekle sorumluyuz.
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {//T sınıfı gerçekten TaskViewModel mi?
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(repository, handle) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

/*
 Bu sınıf ne işe yarar?

 ViewModel'e constructor ile parametre verme işini sağlar.

 Lifecycle ile uyumlu kalır (SavedStateHandle).

 Navigation ile çalışan fragment yapılarında (by viewModels { ... }) parametreli ViewModel kullanımını mümkün kılar.
 */
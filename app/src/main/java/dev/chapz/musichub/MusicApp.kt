package dev.chapz.musichub

import android.app.Application
import android.content.ComponentName
import dev.chapz.musichub.repository.SongRepository
import dev.chapz.musichub.repository.SongRepositoryImpl
import dev.chapz.musichub.service.MediaService
import dev.chapz.musichub.service.MediaServiceConnection
import dev.chapz.musichub.ui.HostViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MusicApp : Application() {

    private val repositoryModule = module {
        single<SongRepository> { SongRepositoryImpl(androidContext().contentResolver) }
    }

    private val mediaModule = module {
        single { MediaServiceConnection.getInstance(androidContext(), ComponentName(androidContext(), MediaService::class.java)) }
    }

    private val viewModelModule = module {
        viewModel { HostViewModel(get()) }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MusicApp)
            modules(repositoryModule, mediaModule, viewModelModule)
        }
    }
}
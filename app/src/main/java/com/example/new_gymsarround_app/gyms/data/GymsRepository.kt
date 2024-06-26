package com.example.new_gymsarround_app.gyms.data
import com.example.new_gymsarround_app.gyms.data.Local.GymsDao
import com.example.new_gymsarround_app.gyms.data.Local.GymsFavouriteState
import com.example.new_gymsarround_app.gyms.data.Local.LocalGym
import com.example.new_gymsarround_app.gyms.data.Remote.GymsApiService
import com.example.new_gymsarround_app.gyms.domain.Gym
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GymsRepository @Inject constructor(
    private val apiService: GymsApiService,
    private var gymsDao :GymsDao
){


     suspend fun taggleFavouriteGym(gymId:Int, state: Boolean) = withContext(
        Dispatchers.IO){
        gymsDao.update(
            GymsFavouriteState(
                id=gymId,
                isFavourite = state
            )
        )
        return@withContext gymsDao.getAll().sortedBy { it.name }
    }

     suspend fun loadGyms() = withContext(Dispatchers.IO){
        try {
            updateLocalDatabase()
        }
        catch (ex:Exception){
            if(gymsDao.getAll().isEmpty()){
                throw Exception("try connect to internet")
            }
        }
    }

    suspend fun getGyms():List<Gym> {
        return withContext(Dispatchers.IO){
            return@withContext gymsDao.getAll().map {
                Gym(it.id,it.name,it.place,it.isOpen,it.isFavourite)
            }
        }
    }

     suspend fun updateLocalDatabase() {
        val gyms = apiService.getGyms()
        val favouriteGymsList =gymsDao.getFavouriteGyms()

        gymsDao.addAll(gyms.map {
            LocalGym(it.id,it.name,it.place,it.isOpen)
        })
        gymsDao.updateAll(
            favouriteGymsList.map{
                GymsFavouriteState(id=it.id,true)
            }
        )
    }


}
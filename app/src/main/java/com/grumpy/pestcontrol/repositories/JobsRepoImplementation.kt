package com.grumpy.pestcontrol.repositories
import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.grumpy.pestcontrol.utils.Constants
import com.grumpy.pestcontrol.utils.Resource
import com.grumpy.pestcontrol.models.Job
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await

class JobsRepoImplementation : JobsRepoInterface{


    private val mJobsCollection = FirebaseFirestore.getInstance().collection(Constants.COLLECTION_JOB)


    //Not used currently
//    override fun getAllJobs() = flow< Resource<List<Job>>>{
//
//        //emit loading state
//        emit(Resource.loading())
//
//        val snapshot = mJobsCollection.get().await()
//        val jobs = snapshot.toObjects(Job::class.java)
//
//        //emit success
//        emit(Resource.success(jobs))
//
//    }.catch {
//        //emit failed along with message
//        emit(Resource.failed(it.message.toString()))
//    }.flowOn(Dispatchers.IO)


    //adds job to database
    override fun addJob(job: Job) = flow<Resource<DocumentReference>>{
        //emit loading state
        emit(Resource.loading())

        val jobRef = mJobsCollection.add(job).await()

        //emit success
        emit(Resource.success(jobRef))

    }.catch {
        emit(Resource.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)


    //retrieves all jobs in database
    override fun getJobs() : Flow<Resource<List<Job>>> = callbackFlow {

      trySend(Resource.loading())

        val eventDoc = mJobsCollection
        val subscription = eventDoc.addSnapshotListener { snapshot, exception ->

            //emit jobs
            if(snapshot != null){
                val jobs = snapshot.toObjects(Job::class.java)
                trySend(Resource.success(jobs)).isSuccess
            }
            //check for errors
            exception?.let {
                trySend(Resource.failed(it.message.toString()))
                cancel(it.message.toString())
            }
        }

        awaitClose {
            //close subscription if UI no longer active
            subscription.remove()
            cancel()
        }
    }


}
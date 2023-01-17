package com.grumpy.pestcontrol.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.grumpy.pestcontrol.utils.Resource
import com.grumpy.pestcontrol.models.Job
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await

class JobsRepoImplementation : JobsRepoInterface{

    private val firebaseAuth : FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private lateinit var uid: String

    init {
        if(firebaseAuth.currentUser != null){
            uid =  firebaseAuth.currentUser!!.uid
//            Log.d("JobRep", uid)
        }
    }

    private val mJobsCollection = FirebaseFirestore.getInstance().collection(uid)


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


    //update entry in database
    override fun updateJob(job: Job) = flow<Resource<Void>> {
        emit(Resource.loading())
        val jobRef = mJobsCollection.document(job.documentId.toString()).set(job).await()
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

    //function to retrieve jobs by date
    override fun getJobByDate(date : String): Flow<Resource<List<Job>>> = callbackFlow {
        trySend(Resource.loading())

        val eventDoc = mJobsCollection.whereEqualTo("date",date)
        val subscription = eventDoc.addSnapshotListener { snapshot, exception ->
            if(snapshot != null){
                val jobs = snapshot.toObjects(Job::class.java)
                trySend(Resource.success(jobs)).isSuccess
            }
            exception?.let {
                trySend(Resource.failed(it.message.toString()))
                cancel(it.message.toString())
            }
        }

        awaitClose{
            subscription.remove()
            cancel()
        }
    }


}
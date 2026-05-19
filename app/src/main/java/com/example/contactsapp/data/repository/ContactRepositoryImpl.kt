package com.example.contactsapp.data.repository

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.net.Uri
import android.os.IBinder
import android.provider.ContactsContract
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.contactsapp.data.mapper.ContactMapper
import com.example.contactsapp.data.service.ContactsService
import com.example.contactsapp.domain.model.Contact
import com.example.contactsapp.domain.repository.ContactsRepository
import com.example.contactsapp.service.IContactsService
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class ContactsRepositoryImpl(
    private val context: Context
) : ContactsRepository {

    companion object {
        private const val TAG = "ContactsRepository"
    }

    private var contactsService: IContactsService? = null
    private var serviceBound = false

    override suspend fun getContacts(): List<Contact> {
        if (ContextCompat.checkSelfPermission(
                context, android.Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.w(TAG, "No READ_CONTACTS permission - returning empty list")
            return emptyList()
        }

        val contacts = mutableListOf<Contact>()

        val cursor = context.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            arrayOf(
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME
            ),
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME + " ASC"
        )

        cursor?.use {
            if (it.count == 0) {
                Log.i(TAG, "Cursor is empty - no contacts found")
                return emptyList()
            }
            it.moveToFirst()

            val idIndex = it.getColumnIndexOrThrow(ContactsContract.Contacts._ID)
            val nameIndex = it.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)

            while (it.moveToNext()) {
                val id = it.getLong(idIndex)
                val displayName = it.getString(nameIndex)
                val contactDetails = getFullContactDetails(id)

                if (displayName.isNotBlank() || contactDetails.phone != null || contactDetails.email != null) {
                    contacts.add(
                        Contact(
                            id = id,
                            displayName = displayName,
                            phoneNumber = contactDetails.phone,
                            email = contactDetails.email,
                            address = contactDetails.address,
                            company = contactDetails.company,
                            department = contactDetails.department,
                            jobTitle = contactDetails.jobTitle,
                            notes = contactDetails.notes
                        )
                    )
                }
            }
            Log.d(TAG, "Loaded ${contacts.size} contacts")
        } ?: run {
            Log.e(TAG, "Cursor is null")
            return emptyList()
        }

        return contacts
    }

    override suspend fun getContactById(contactId: Long): Contact? {
        if (ContextCompat.checkSelfPermission(
                context, android.Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.w(TAG, "No READ_CONTACTS permission")
            return null
        }

        return try {
            val contactDetails = getFullContactDetails(contactId)

            val nameCursor = context.contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                arrayOf(ContactsContract.Contacts.DISPLAY_NAME),
                "${ContactsContract.Contacts._ID} = ?",
                arrayOf(contactId.toString()),
                null
            )

            var displayName = ""
            nameCursor?.use {
                if (it.moveToFirst()) {
                    displayName = it.getString(0) ?: ""
                }
            }

            Contact(
                id = contactId,
                displayName = displayName,
                phoneNumber = contactDetails.phone,
                email = contactDetails.email,
                address = contactDetails.address,
                company = contactDetails.company,
                department = contactDetails.department,
                jobTitle = contactDetails.jobTitle,
                notes = contactDetails.notes
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error getting contact by ID: ${e.message}", e)
            null
        }
    }

    override suspend fun deleteDuplicateContacts(): Int {
        return bindAndCallService()
    }

    private suspend fun bindAndCallService(): Int = suspendCancellableCoroutine { continuation ->
        val intent = Intent(context, ContactsService::class.java).apply {
            action = "com.example.contactsapp.service.IContactsService"
            setPackage(context.packageName)
        }

        var serviceConnection: ServiceConnection? = null

        serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                contactsService = IContactsService.Stub.asInterface(service)
                serviceBound = true

                try {
                    val result = contactsService?.deleteDuplicateContacts()
                        ?: ContactsService.RESULT_ERROR

                    Log.d(TAG, "Service returned result: $result")

                    serviceConnection?.let { unbindService(it) }

                    if (continuation.isActive) {
                        continuation.resume(result)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error calling service: ${e.message}", e)
                    serviceConnection?.let { unbindService(it) }
                    if (continuation.isActive) {
                        continuation.resume(ContactsService.RESULT_ERROR)
                    }
                }
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                Log.d(TAG, "Service disconnected")
                contactsService = null
                serviceBound = false
            }
        }

        val bound = context.bindService(intent, serviceConnection!!, Context.BIND_AUTO_CREATE)

        if (!bound) {
            Log.e(TAG, "Failed to bind to service")
            if (continuation.isActive) {
                continuation.resume(ContactsService.RESULT_ERROR)
            }
        }

        continuation.invokeOnCancellation {
            Log.d(TAG, "Coroutine cancelled - unbinding service")
            serviceConnection?.let { unbindService(it) }
        }
    }

    
    private fun getFullContactDetails(contactId: Long): ContactDetails {
        var phone: String? = null
        var email: String? = null
        var address: String? = null
        var company: String? = null
        var department: String? = null
        var jobTitle: String? = null
        var notes: String? = null

        
        val phoneCursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
            "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
            arrayOf(contactId.toString()),
            null
        )
        phoneCursor?.use {
            if (it.moveToFirst()) {
                phone = it.getString(0)
            }
        }

        
        val emailCursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Email.DATA),
            "${ContactsContract.CommonDataKinds.Email.CONTACT_ID} = ?",
            arrayOf(contactId.toString()),
            null
        )
        emailCursor?.use {
            if (it.moveToFirst()) {
                email = it.getString(0)
            }
        }

        
        val dataCursor = context.contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            arrayOf(
                ContactsContract.Data.MIMETYPE,
                ContactsContract.Data.DATA1,
                ContactsContract.Data.DATA2,
                ContactsContract.Data.DATA3,
                ContactsContract.Data.DATA4
            ),
            "${ContactsContract.Data.CONTACT_ID} = ?",
            arrayOf(contactId.toString()),
            null
        )

        dataCursor?.use {
            while (it.moveToNext()) {
                val mimeType = it.getString(0)
                val data1 = it.getString(1)
                val data2 = it.getString(2)
                val data3 = it.getString(3)
                val data4 = it.getString(4)

                when (mimeType) {
                    
                    ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE -> {
                        address = buildString {
                            if (!data1.isNullOrBlank()) append(data1)
                            if (!data2.isNullOrBlank()) {
                                if (isNotEmpty()) append(", ")
                                append(data2)
                            }
                            if (!data3.isNullOrBlank()) {
                                if (isNotEmpty()) append(", ")
                                append(data3)
                            }
                            if (!data4.isNullOrBlank()) {
                                if (isNotEmpty()) append(", ")
                                append(data4)
                            }
                        }.takeIf { it.isNotBlank() }
                    }

                    
                    ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE -> {
                        if (company.isNullOrBlank()) {
                            company = data1
                        }
                        if (department.isNullOrBlank()) {
                            department = data2
                        }
                        if (jobTitle.isNullOrBlank()) {
                            jobTitle = data3
                        }
                    }

                    
                    ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE -> {
                        if (notes.isNullOrBlank()) {
                            notes = data1
                        }
                    }
                }
            }
        }

        return ContactDetails(phone, email, address, company, department, jobTitle, notes)
    }

    private fun getContactDetails(contactId: Long): Pair<String?, String?> {
        var phone: String? = null
        var email: String? = null

        val phoneCursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
            "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
            arrayOf(contactId.toString()),
            null
        )
        phoneCursor?.use {
            if (it.moveToFirst()) {
                phone = it.getString(0)
            }
        }

        val emailCursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Email.DATA),
            "${ContactsContract.CommonDataKinds.Email.CONTACT_ID} = ?",
            arrayOf(contactId.toString()),
            null
        )
        emailCursor?.use {
            if (it.moveToFirst()) {
                email = it.getString(0)
            }
        }

        return Pair(phone, email)
    }

    private fun unbindService(serviceConnection: ServiceConnection) {
        if (serviceBound) {
            try {
                context.unbindService(serviceConnection)
                Log.d(TAG, "Service successfully unbound")
            } catch (e: IllegalArgumentException) {
                Log.w(TAG, "Service already unbound: ${e.message}")
            } finally {
                serviceBound = false
                contactsService = null
            }
        }
    }

    private data class ContactDetails(
        val phone: String?,
        val email: String?,
        val address: String?,
        val company: String?,
        val department: String?,
        val jobTitle: String?,
        val notes: String?
    )
}
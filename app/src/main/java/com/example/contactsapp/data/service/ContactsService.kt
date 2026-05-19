package com.example.contactsapp.data.service

import android.app.Service
import android.content.ContentResolver
import android.content.Intent
import android.os.IBinder
import android.os.RemoteException
import android.provider.ContactsContract
import android.util.Log
import com.example.contactsapp.service.IContactsService

class ContactsService : Service() {

    companion object {
        private const val TAG = "ContactsService"

        const val RESULT_SUCCESS = 0
        const val RESULT_ERROR = 1
        const val RESULT_NOT_FOUND = 2
    }

    private val binder = object : IContactsService.Stub() {

        @Throws(RemoteException::class)
        override fun deleteDuplicateContacts(): Int {
            return try {
                Log.d(TAG, "Starting duplicate deletion process")
                performDeleteDuplicates()
            } catch (e: Exception) {
                Log.e(TAG, "Error deleting duplicates: ${e.message}", e)
                RESULT_ERROR
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    
    private fun performDeleteDuplicates(): Int {
        val contentResolver: ContentResolver = contentResolver
        val seenContacts = mutableMapOf<String, Long>()
        var deletedCount = 0

        val cursor = contentResolver.query(
            ContactsContract.RawContacts.CONTENT_URI,
            arrayOf(
                ContactsContract.RawContacts._ID,
                ContactsContract.RawContacts.CONTACT_ID
            ),
            null,
            null,
            null
        )

        cursor?.use {
            val idIndex = it.getColumnIndexOrThrow(ContactsContract.RawContacts._ID)

            if (it.moveToFirst()) {
                do {
                    val rawContactId = it.getLong(idIndex)
                    val contactData = getContactDetails(contentResolver, rawContactId)

                    if (contactData != null && contactData.hasAllFields()) {
                        val fingerprint = createFingerprint(contactData)

                        if (seenContacts.containsKey(fingerprint)) {
                            val rowsDeleted = contentResolver.delete(
                                ContactsContract.RawContacts.CONTENT_URI,
                                "${ContactsContract.RawContacts._ID} = ?",
                                arrayOf(rawContactId.toString())
                            )
                            if (rowsDeleted > 0) {
                                deletedCount++
                                Log.d(TAG, "Deleted duplicate ID: $rawContactId")
                            }
                        } else {
                             seenContacts[fingerprint] = rawContactId
                        }
                    }
                } while (it.moveToNext())
            }
        }

        Log.d(TAG, "Process finished. Deleted: $deletedCount contacts")

        return if (deletedCount > 0) {
            RESULT_SUCCESS
        } else {
            RESULT_NOT_FOUND
        }
    }

    
    private fun getContactDetails(cr: ContentResolver, rawContactId: Long): ContactData? {
        var displayName: String? = null
        var phoneNumber: String? = null
        var email: String? = null

        val cursor = cr.query(
            ContactsContract.Data.CONTENT_URI,
            arrayOf(
                ContactsContract.Data.MIMETYPE,
                ContactsContract.Data.DATA1,
                ContactsContract.Data.DATA2,
                ContactsContract.Data.DATA3
            ),
            "${ContactsContract.Data.RAW_CONTACT_ID} = ?",
            arrayOf(rawContactId.toString()),
            null
        )

        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    val mimeType = it.getString(0)
                    val data = it.getString(1)

                    when (mimeType) {
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE -> {
                            val firstName = it.getString(2) ?: ""
                            val lastName = it.getString(3) ?: ""
                            displayName = "$firstName $lastName".trim()
                        }
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE -> {
                            if (phoneNumber.isNullOrBlank()) {
                                phoneNumber = data?.normalizePhoneNumber()
                            }
                        }
                        ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE -> {
                            if (email.isNullOrBlank()) {
                                email = data?.lowercase()?.trim()
                            }
                        }
                    }
                } while (it.moveToNext())
            }
        }

        if (displayName.isNullOrBlank() && phoneNumber.isNullOrBlank()) {
            return null
        }

        return ContactData(
            name = displayName ?: "",
            phone = phoneNumber ?: "",
            email = email ?: ""
        )
    }

    
    private fun createFingerprint(data: ContactData): String {
        val name = data.name.lowercase().trim()
        val phone = data.phone.normalizePhoneNumber()
        val email = data.email.lowercase().trim()

        return "$name|$phone|$email"
    }

    private fun String.normalizePhoneNumber(): String {
        val digits = replace(Regex("[^\\d+]"), "")
        return if (digits.startsWith("8") && digits.length == 11) {
            "+7" + digits.substring(1)
        } else if (digits.startsWith("7") && digits.length == 11 && !digits.startsWith("+")) {
            "+7" + digits.substring(1)
        } else if (!digits.startsWith("+") && digits.length == 10) {
            "+7" + digits
        } else {
            digits
        }
    }

    private data class ContactData(
        val name: String,
        val phone: String,
        val email: String
    ) {
        fun hasAllFields(): Boolean {
            return name.isNotBlank() && phone.isNotBlank() && email.isNotBlank()
        }
    }
}
package com.spym.navchenta_welcome.navigation.ui.contact_us

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.spym.navchenta_welcome.R

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class Contact_usFragment : Fragment() {

    private lateinit var call: TextView
    private lateinit var feedback: TextView
    private lateinit var sendYourMessage: TextView
    private lateinit var facebook: ImageView
    private lateinit var instagram: ImageView
    private lateinit var youtube: ImageView
    private lateinit var share: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.contact, container, false)

        call = v.findViewById(R.id.call)
        call.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:+91 8383066031")
            startActivity(callIntent)
        }

        feedback = v.findViewById(R.id.feedback)
        feedback.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            val uriText = "mailto:" + Uri.encode("info@seekmyvision.in") + "?subject=" +
                    Uri.encode("your email id ") + "&body="
            val uri = Uri.parse(uriText)
            intent.data = uri
            startActivity(Intent.createChooser(intent, "Send Email"))
        }

        sendYourMessage = v.findViewById(R.id.send_your_message)
//        sendYourMessage.setOnClickListener {
//            val intent = Intent(requireContext(), query_page::class.java)
//            startActivity(intent)
//        }

        facebook = v.findViewById(R.id.facebook)
        facebook.setOnClickListener {
            gotoUrl("https://www.facebook.com/seekmyvision")
        }

        youtube = v.findViewById(R.id.youtube)
        youtube.setOnClickListener {
            gotoUrl("https://www.youtube.com/channel/UC9Zs03cVzZzBRNDJ_pmpv9w")
        }

        instagram = v.findViewById(R.id.instagram)
        instagram.setOnClickListener {
            gotoUrl("https://www.instagram.com/seekmyvision.in/")
        }

        share = v.findViewById(R.id.share)
        share.setOnClickListener {
            val i = Intent(Intent.ACTION_SEND)
            i.type = "text/plain"
            val sharebody = "look all Programmings"
            val subject =
                "https://play.google.com/store/apps/details?id=in.seekmyvision.seekmyvision"
            i.putExtra(Intent.EXTRA_SUBJECT, sharebody)
            i.putExtra(Intent.EXTRA_TEXT, subject)
            startActivity(Intent.createChooser(i, "Seek my vision"))
        }

        return v
    }

    private fun gotoUrl(url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Contact_usFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Contact_usFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
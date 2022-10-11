package com.alief.passman.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alief.passman.R
import com.alief.passman.adaptors.CardAdaptor
import com.alief.passman.adaptors.OnBankCardItemClick
import com.alief.passman.database.MyDao
import com.alief.passman.database.MyDataBase
import com.alief.passman.models.BankCardModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class CardFragment : Fragment(), OnBankCardItemClick, OnBottomSheetCloseListener {

    private var mContext: Context? = null
    lateinit var recyclerView: RecyclerView
    lateinit var adaptor: CardAdaptor
    lateinit var list: MutableList<BankCardModel>
    lateinit var dao: MyDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_card, container, false)

        dao = MyDataBase.getInstance(mContext!!).getDao()!!
        list = dao.getBankCards()

        recyclerView = view.findViewById(R.id.card_recycler)
        adaptor = CardAdaptor(list, this, mContext!!)

        recyclerView.adapter = adaptor
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        recyclerView.setHasFixedSize(true)

        //FAD onClick
        val fad: FloatingActionButton = view.findViewById(R.id.card_fad)
        fad.setOnClickListener {
            val bottomSheet = AddCardBottomSheet(this)
            bottomSheet.show(parentFragmentManager, "add card")
        }
        return view
    }

    override fun onDeleteImageCard(position: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            val card = dao.getBankCardById(list[position].id)

            dao.deleteBankCard(card)
            list.removeAt(position)
            adaptor.notifyItemRemoved(position)

        }
    }

    override fun onUpdateImageCard(position: Int) {
       val bottomSheet = AddCardBottomSheet(this, list[position].id)
        bottomSheet.show(parentFragmentManager, "sheet")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onDetach() {
        super.onDetach()
        mContext = null
    }

    override fun onDismiss() {
        val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
        ft.replace(R.id.frame_layout, CardFragment())
        ft.commit()
    }
}
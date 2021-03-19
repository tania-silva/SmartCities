package com.example.smartcities.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.smartcities.ENTITIES.Notas
import com.example.smartcities.R
import com.example.smartcities.VerNotas
import kotlinx.android.synthetic.main.recyclerline.view.*

const val TITULO="TITULO"
const val DESCRICAO="DESCRICAO"
const val ID="ID"


class NotasAdapter internal constructor(
        context: Context
) : RecyclerView.Adapter<NotasAdapter.NotasViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var notas = emptyList<Notas>() // Cached copy of notas

    interface CallbackInterface {
        fun passResultCallback(id: Int?)
    }

    class NotasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titulo: TextView = itemView.findViewById(R.id.titulo)
        val descricao: ConstraintLayout = itemView.findViewById(R.id.recycler_line)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotasViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerline, parent, false)
        return NotasViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NotasViewHolder, position: Int) {
        val current = notas[position]

        holder.titulo.text = current.titulo
        val id: Int? = current.id

        holder.descricao.setOnClickListener{
            val context = holder.titulo.context
            val t = holder.titulo.text.toString()
            val d = current.descricao

            val intent = Intent(context,VerNotas::class.java).apply {
                putExtra(TITULO, t)
                putExtra(DESCRICAO, d)
                putExtra(ID, id)
            }
            context.startActivity(intent)
        }
    }

    internal fun setNotas(notas: List<Notas>) {
        this.notas = notas
        notifyDataSetChanged()
    }

    override fun getItemCount() = notas.size
}

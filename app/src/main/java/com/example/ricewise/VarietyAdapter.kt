package com.example.ricewise

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ricewise.databinding.ItemVarietyBinding

class VarietyAdapter(
    private val onEditClick: (Variety) -> Unit,
    private val onDeleteClick: (Variety) -> Unit
) : ListAdapter<Variety, VarietyAdapter.VarietyViewHolder>(VarietyDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VarietyViewHolder {
        val binding = ItemVarietyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VarietyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VarietyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VarietyViewHolder(private val binding: ItemVarietyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(variety: Variety) {
            binding.tvName.text = variety.name
            binding.tvCode.text = "Code: ${variety.code}"
            binding.tvType.text = "Type: ${variety.type ?: "N/A"}"
            binding.tvMaturityDays.text = "Maturity: ${variety.maturity_days ?: "N/A"} days"
            binding.tvGrainLength.text = "Grain Length: ${variety.grain_length ?: "N/A"}"
            binding.tvGrainShape.text = "Grain Shape: ${variety.grain_shape ?: "N/A"}"
            binding.tvAmyloseContent.text = "Amylose: ${variety.amylose_content ?: "N/A"}"
            binding.tvYield.text = "Yield: ${variety.yield_per_hectare ?: "N/A"} kg/ha"
            binding.tvResistance.text = "Resistance: ${variety.resistance.joinToString()}"
            binding.tvRecommendedArea.text = "Area: ${variety.recommended_area ?: "N/A"}"
            binding.tvReleasedYear.text = "Released: ${variety.released_year ?: "N/A"}"
            binding.tvDescription.text = "Description: ${variety.description ?: "N/A"}"

            binding.btnEdit.setOnClickListener { onEditClick(variety) }
            binding.btnDelete.setOnClickListener { onDeleteClick(variety) }
        }
    }
}

class VarietyDiffCallback : DiffUtil.ItemCallback<Variety>() {
    override fun areItemsTheSame(oldItem: Variety, newItem: Variety): Boolean {
        return oldItem._id == newItem._id
    }

    override fun areContentsTheSame(oldItem: Variety, newItem: Variety): Boolean {
        return oldItem == newItem
    }
}
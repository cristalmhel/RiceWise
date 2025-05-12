package com.example.ricewise

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.ricewise.databinding.FragmentVarietiesBinding

class VarietiesFragment : Fragment() {
    private var _binding: FragmentVarietiesBinding? = null
    private val binding get() = _binding!!
    private lateinit var varietyAdapter: VarietyAdapter
    private var editingId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVarietiesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupForm()
        fetchVarieties()

        binding.btnToggleForm.setOnClickListener {
            toggleFormVisibility()
        }

        binding.btnSubmit.setOnClickListener {
            submitVariety()
        }
    }

    private fun setupRecyclerView() {
        varietyAdapter = VarietyAdapter(
            onEditClick = { variety -> populateFormForEdit(variety) },
            onDeleteClick = { variety ->
                val id = variety._id
                if (!id.isNullOrBlank()) {
                    deleteVariety(id)
                } else {
                    Toast.makeText(context, "Cannot delete: missing ID", Toast.LENGTH_SHORT).show()
                }
            }
        )
        binding.rvVarieties.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = varietyAdapter
        }
    }

    private fun setupForm() {
        // Setup spinners
        val types = arrayOf("", "indica", "japonica")
        val grainLengths = arrayOf("", "long", "medium", "short")
        val grainShapes = arrayOf("", "slender", "bold")
        val amyloseContents = arrayOf("", "high", "medium", "low")
        val recommendedAreas = arrayOf("", "lowland", "upland", "flood-prone")

        binding.spinnerType.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, types)
        binding.spinnerGrainLength.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, grainLengths)
        binding.spinnerGrainShape.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, grainShapes)
        binding.spinnerAmyloseContent.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, amyloseContents)
        binding.spinnerRecommendedArea.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, recommendedAreas)
    }

    private fun fetchVarieties() {
        RetrofitClient.apiService.getAllVarieties().enqueue(object : Callback<List<Variety>> {
            override fun onResponse(call: Call<List<Variety>>, response: Response<List<Variety>>) {
                if (response.isSuccessful) {
                    varietyAdapter.submitList(response.body() ?: emptyList())
                } else {
                    Toast.makeText(context, "Failed to fetch varieties", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Variety>>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun submitVariety() {
        val variety = if (editingId.isNullOrEmpty()) {
            Variety(
                name = binding.etName.text.toString().trim(),
                code = binding.etCode.text.toString().trim(),
                type = binding.spinnerType.selectedItem.toString().takeIf { it.isNotEmpty() },
                maturity_days = binding.etMaturityDays.text.toString().toIntOrNull(),
                grain_length = binding.spinnerGrainLength.selectedItem.toString().takeIf { it.isNotEmpty() },
                grain_shape = binding.spinnerGrainShape.selectedItem.toString().takeIf { it.isNotEmpty() },
                amylose_content = binding.spinnerAmyloseContent.selectedItem.toString().takeIf { it.isNotEmpty() },
                yield_per_hectare = binding.etYieldPerHectare.text.toString().toIntOrNull(),
                resistance = binding.etResistance.text.toString().split(",").map { it.trim() }.filter { it.isNotEmpty() },
                recommended_area = binding.spinnerRecommendedArea.selectedItem.toString().takeIf { it.isNotEmpty() },
                released_year = binding.etReleasedYear.text.toString().toIntOrNull(),
                description = binding.etDescription.text.toString().trim()
            )
        } else {
            Variety(
                _id = editingId ?: "",
                name = binding.etName.text.toString().trim(),
                code = binding.etCode.text.toString().trim(),
                type = binding.spinnerType.selectedItem.toString().takeIf { it.isNotEmpty() },
                maturity_days = binding.etMaturityDays.text.toString().toIntOrNull(),
                grain_length = binding.spinnerGrainLength.selectedItem.toString().takeIf { it.isNotEmpty() },
                grain_shape = binding.spinnerGrainShape.selectedItem.toString().takeIf { it.isNotEmpty() },
                amylose_content = binding.spinnerAmyloseContent.selectedItem.toString().takeIf { it.isNotEmpty() },
                yield_per_hectare = binding.etYieldPerHectare.text.toString().toIntOrNull(),
                resistance = binding.etResistance.text.toString().split(",").map { it.trim() }.filter { it.isNotEmpty() },
                recommended_area = binding.spinnerRecommendedArea.selectedItem.toString().takeIf { it.isNotEmpty() },
                released_year = binding.etReleasedYear.text.toString().toIntOrNull(),
                description = binding.etDescription.text.toString().trim()
            )
        }

        if (variety.name.isEmpty() || variety.code.isEmpty()) {
            Toast.makeText(context, "Name and Code are required", Toast.LENGTH_SHORT).show()
            return
        }

        val call = if (editingId != null) {
            Log.d("dddd", "ddddd")
            RetrofitClient.apiService.updateVarietyById(editingId!!, variety)
        } else {
            RetrofitClient.apiService.createVariety(variety)
        }

        call.enqueue(object : Callback<Variety> {
            override fun onResponse(call: Call<Variety>, response: Response<Variety>) {
                if (response.isSuccessful) {
                    fetchVarieties()
                    clearForm()
                    toggleFormVisibility()
                    Toast.makeText(context, if (editingId != null) "Variety updated" else "Variety added", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to save variety", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Variety>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteVariety(id: String) {
        RetrofitClient.apiService.deleteVarietyById(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    fetchVarieties()
                    Toast.makeText(context, "Variety deleted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to delete variety", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun populateFormForEdit(variety: Variety) {
        editingId = variety._id
        binding.etName.setText(variety.name)
        binding.etCode.setText(variety.code)
        binding.spinnerType.setSelection((binding.spinnerType.adapter as ArrayAdapter<String>).getPosition(variety.type ?: ""))
        binding.etMaturityDays.setText(variety.maturity_days?.toString() ?: "")
        binding.spinnerGrainLength.setSelection((binding.spinnerGrainLength.adapter as ArrayAdapter<String>).getPosition(variety.grain_length ?: ""))
        binding.spinnerGrainShape.setSelection((binding.spinnerGrainShape.adapter as ArrayAdapter<String>).getPosition(variety.grain_shape ?: ""))
        binding.spinnerAmyloseContent.setSelection((binding.spinnerAmyloseContent.adapter as ArrayAdapter<String>).getPosition(variety.amylose_content ?: ""))
        binding.etYieldPerHectare.setText(variety.yield_per_hectare?.toString() ?: "")
        binding.etResistance.setText(variety.resistance.joinToString(", "))
        binding.spinnerRecommendedArea.setSelection((binding.spinnerRecommendedArea.adapter as ArrayAdapter<String>).getPosition(variety.recommended_area ?: ""))
        binding.etReleasedYear.setText(variety.released_year?.toString() ?: "")
        binding.etDescription.setText(variety.description)
        binding.btnSubmit.text = "Update Variety"
        binding.formContainer.visibility = View.VISIBLE
        binding.btnToggleForm.apply {
            setBackgroundResource(R.drawable.rounded_button_fill_gray)
            setTextColor(ContextCompat.getColor(context, android.R.color.black))
        }
        binding.btnToggleForm.text = "Cancel"
    }

    private fun clearForm() {
        editingId = null
        binding.etName.text.clear()
        binding.etCode.text.clear()
        binding.spinnerType.setSelection(0)
        binding.etMaturityDays.text.clear()
        binding.spinnerGrainLength.setSelection(0)
        binding.spinnerGrainShape.setSelection(0)
        binding.spinnerAmyloseContent.setSelection(0)
        binding.etYieldPerHectare.text.clear()
        binding.etResistance.text.clear()
        binding.spinnerRecommendedArea.setSelection(0)
        binding.etReleasedYear.text.clear()
        binding.etDescription.text.clear()
        binding.btnSubmit.text = "Add Variety"
    }

    private fun toggleFormVisibility() {
        if (binding.formContainer.visibility == View.VISIBLE) {
            binding.formContainer.visibility = View.GONE
            binding.btnToggleForm.text = "Add New Variety"
            binding.btnToggleForm.apply {
                setBackgroundResource(R.drawable.rounded_button_fill)
                setTextColor(ContextCompat.getColor(context, android.R.color.white))
            }
            clearForm()
        } else {
            binding.formContainer.visibility = View.VISIBLE
            binding.btnToggleForm.apply {
                setBackgroundResource(R.drawable.rounded_button_fill_gray)
                setTextColor(ContextCompat.getColor(context, android.R.color.black))
            }
            binding.btnToggleForm.text = "Cancel"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
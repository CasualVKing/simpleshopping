package com.simpleshopping.dialog

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.simpleshopping.R
import com.simpleshopping.ShoppingListViewModel
import com.simpleshopping.data.Section
import com.simpleshopping.databinding.DialogAddItemBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AddItemDialogFragment : DialogFragment() {

    private val viewModel: ShoppingListViewModel by activityViewModels()
    private var _binding: DialogAddItemBinding? = null
    private val binding get() = _binding!!

    private var sections: List<Section> = emptyList()
    private var selectedSection: Section? = null
    private var autocompleteJob: Job? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogAddItemBinding.inflate(layoutInflater)

        setupSectionDropdown()
        setupAutocomplete()

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.add_item_title)
            .setView(binding.root)
            .setPositiveButton(R.string.add) { _, _ ->
                val name = binding.itemNameInput.text.toString().trim()
                val isRecurring = binding.recurringSwitch.isChecked
                if (name.isNotEmpty() && selectedSection != null) {
                    viewModel.addItem(name, selectedSection!!.id, isRecurring)
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
    }

    private fun setupSectionDropdown() {
        lifecycleScope.launch {
            viewModel.sections.collect { sectionList ->
                sections = sectionList
                val names = sectionList.map { it.name }
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, names)
                binding.sectionDropdown.setAdapter(adapter)

                // Default to first section
                if (selectedSection == null && sectionList.isNotEmpty()) {
                    selectedSection = sectionList[0]
                    binding.sectionDropdown.setText(sectionList[0].name, false)
                }
            }
        }

        binding.sectionDropdown.setOnItemClickListener { _, _, position, _ ->
            selectedSection = sections.getOrNull(position)
        }
    }

    private fun setupAutocomplete() {
        binding.itemNameInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val query = s?.toString()?.trim() ?: return
                if (query.length < 2) return

                autocompleteJob?.cancel()
                autocompleteJob = lifecycleScope.launch {
                    delay(300) // debounce
                    val results = viewModel.searchHistory(query)
                    if (results.isNotEmpty()) {
                        val names = results.map { it.name }
                        val adapter = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_dropdown_item_1line,
                            names
                        )
                        binding.itemNameInput.setAdapter(adapter)
                        if (binding.itemNameInput.hasFocus()) {
                            binding.itemNameInput.showDropDown()
                        }
                    }
                }
            }
        })

        binding.itemNameInput.setOnItemClickListener { _, _, position, _ ->
            // When user picks from autocomplete, also set the section
            val selectedName = binding.itemNameInput.text.toString()
            lifecycleScope.launch {
                val results = viewModel.searchHistory(selectedName)
                val match = results.firstOrNull { it.name == selectedName }
                if (match != null) {
                    val section = sections.firstOrNull { it.id == match.sectionId }
                    if (section != null) {
                        selectedSection = section
                        binding.sectionDropdown.setText(section.name, false)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

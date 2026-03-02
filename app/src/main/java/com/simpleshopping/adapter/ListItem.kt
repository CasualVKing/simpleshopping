package com.simpleshopping.adapter

import com.simpleshopping.data.Item
import com.simpleshopping.data.Section

sealed class ListItem {
    data class SectionHeader(val section: Section, val isCollapsed: Boolean = false) : ListItem()
    data class ShoppingItem(val item: Item) : ListItem()
    data class InlineInput(val sectionId: Long) : ListItem()
}

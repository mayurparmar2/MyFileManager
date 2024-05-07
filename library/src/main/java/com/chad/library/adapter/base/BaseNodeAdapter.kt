package com.chad.library.adapter.base

import androidx.annotation.IntRange
import androidx.recyclerview.widget.DiffUtil
import com.chad.library.adapter.base.entity.node.BaseExpandNode
import com.chad.library.adapter.base.entity.node.BaseNode
import com.chad.library.adapter.base.entity.node.NodeFooterImp
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.provider.BaseNodeProvider

abstract class BaseNodeAdapter(nodeList: MutableList<BaseNode>? = null)
    : BaseProviderMultiAdapter<BaseNode>(null) {

    private val fullSpanNodeTypeSet = HashSet<Int>()

    init {
        if (!nodeList.isNullOrEmpty()) {
            val flatData = flatData(nodeList)
            this.data.addAll(flatData)
        }
    }


    
    fun addNodeProvider(provider: BaseNodeProvider) {
        addItemProvider(provider)
    }

    
    fun addFullSpanNodeProvider(provider: BaseNodeProvider) {
        fullSpanNodeTypeSet.add(provider.itemViewType)
        addItemProvider(provider)
    }

    
    fun addFooterNodeProvider(provider: BaseNodeProvider) {
        addFullSpanNodeProvider(provider)
    }

    
    override fun addItemProvider(provider: BaseItemProvider<BaseNode>) {
        if (provider is BaseNodeProvider) {
            super.addItemProvider(provider)
        } else {
            throw IllegalStateException("Please add BaseNodeProvider, no BaseItemProvider!")
        }
    }

    override fun isFixedViewType(type: Int): Boolean {
        return super.isFixedViewType(type) || fullSpanNodeTypeSet.contains(type)
    }

    override fun setNewInstance(list: MutableList<BaseNode>?) {
        super.setNewInstance(flatData(list ?: arrayListOf()))
    }

    
    override fun setList(list: Collection<BaseNode>?) {
        super.setList(flatData(list ?: arrayListOf()))
    }

    
    override fun addData(position: Int, data: BaseNode) {
        addData(position, arrayListOf(data))
    }

    override fun addData(data: BaseNode) {
        addData(arrayListOf(data))
    }

    override fun addData(position: Int, newData: Collection<BaseNode>) {
        val nodes = flatData(newData)
        super.addData(position, nodes)
    }

    override fun addData(newData: Collection<BaseNode>) {
        val nodes = flatData(newData)
        super.addData(nodes)
    }

    
    override fun removeAt(position: Int) {
        val removeCount = removeNodesAt(position)
        notifyItemRangeRemoved(position + headerLayoutCount, removeCount)
        compatibilityDataSizeChanged(0)
    }

    
    override fun setData(index: Int, data: BaseNode) {
        
        val removeCount = removeNodesAt(index)

        val newFlatData = flatData(arrayListOf(data))
        this.data.addAll(index, newFlatData)

        if (removeCount == newFlatData.size) {
            notifyItemRangeChanged(index + headerLayoutCount, removeCount)
        } else {
            notifyItemRangeRemoved(index + headerLayoutCount, removeCount)
            notifyItemRangeInserted(index + headerLayoutCount, newFlatData.size)


        }
    }

    override fun setDiffNewData(list: MutableList<BaseNode>?) {
        if (hasEmptyView()) {
            setNewInstance(list)
            return
        }
        super.setDiffNewData(flatData(list ?: arrayListOf()))
    }

    override fun setDiffNewData(diffResult: DiffUtil.DiffResult, list: MutableList<BaseNode>) {
        if (hasEmptyView()) {
            setNewInstance(list)
            return
        }
        super.setDiffNewData(diffResult, flatData(list))
    }

    
    private fun removeNodesAt(position: Int): Int {
        if (position >= data.size) {
            return 0
        }
        
        var removeCount = 0

        
        removeCount = removeChildAt(position)

        
        this.data.removeAt(position)
        removeCount += 1

        val node = this.data[position]
        
        if (node is NodeFooterImp && node.footerNode != null) {
            this.data.removeAt(position)
            removeCount += 1
        }
        return removeCount
    }

    private fun removeChildAt(position: Int): Int {
        if (position >= data.size) {
            return 0
        }
        
        var removeCount = 0

        val node = this.data[position]
        
        if (!node.childNode.isNullOrEmpty()) {
            if (node is BaseExpandNode) {
                if (node.isExpanded) {
                    val items = flatData(node.childNode!!)
                    this.data.removeAll(items)
                    removeCount = items.size
                }
            } else {
                val items = flatData(node.childNode!!)
                this.data.removeAll(items)
                removeCount = items.size
            }
        }
        return removeCount
    }

    


    

    
    fun nodeAddData(parentNode: BaseNode, data: BaseNode) {
        parentNode.childNode?.let {
            it.add(data)

            if (parentNode is BaseExpandNode && !parentNode.isExpanded) {
                return
            }

            val parentIndex = this.data.indexOf(parentNode)
            val childIndex = it.size
            addData(parentIndex + childIndex, data)
        }
    }

    
    fun nodeAddData(parentNode: BaseNode, childIndex: Int, data: BaseNode) {
        parentNode.childNode?.let {
            it.add(childIndex, data)

            if (parentNode is BaseExpandNode && !parentNode.isExpanded) {
                return
            }

            val parentIndex = this.data.indexOf(parentNode)
            val pos = parentIndex + 1 + childIndex
            addData(pos, data)
        }
    }

    
    fun nodeAddData(parentNode: BaseNode, childIndex: Int, newData: Collection<BaseNode>) {
        parentNode.childNode?.let {
            it.addAll(childIndex, newData)

            if (parentNode is BaseExpandNode && !parentNode.isExpanded) {
                return
            }
            val parentIndex = this.data.indexOf(parentNode)
            val pos = parentIndex + 1 + childIndex
            addData(pos, newData)
        }
    }

    
    fun nodeRemoveData(parentNode: BaseNode, childIndex: Int) {
        parentNode.childNode?.let {
            if (childIndex >= it.size) {
                return
            }

            if (parentNode is BaseExpandNode && !parentNode.isExpanded) {
                it.removeAt(childIndex)
                return
            }

            val parentIndex = this.data.indexOf(parentNode)
            val pos = parentIndex + 1 + childIndex
            remove(pos)

            it.removeAt(childIndex)
        }
    }

    
    fun nodeRemoveData(parentNode: BaseNode, childNode: BaseNode) {
        parentNode.childNode?.let {
            if (parentNode is BaseExpandNode && !parentNode.isExpanded) {
                it.remove(childNode)
                return
            }
            remove(childNode)

            it.remove(childNode)
        }
    }

    
    fun nodeSetData(parentNode: BaseNode, childIndex: Int, data: BaseNode) {
        parentNode.childNode?.let {
            if (childIndex >= it.size) {
                return
            }

            if (parentNode is BaseExpandNode && !parentNode.isExpanded) {
                it[childIndex] = data
                return
            }

            val parentIndex = this.data.indexOf(parentNode)
            val pos = parentIndex + 1 + childIndex
            setData(pos, data)

            it[childIndex] = data
        }
    }

    
    fun nodeReplaceChildData(parentNode: BaseNode, newData: Collection<BaseNode>) {
        parentNode.childNode?.let {
            if (parentNode is BaseExpandNode && !parentNode.isExpanded) {
                it.clear()
                it.addAll(newData)
                return
            }

            val parentIndex = this.data.indexOf(parentNode)
            val removeCount = removeChildAt(parentIndex)

            it.clear()
            it.addAll(newData)

            val newFlatData = flatData(newData)
            this.data.addAll(parentIndex + 1, newFlatData)

            val positionStart = parentIndex + 1 + headerLayoutCount
            if (removeCount == newFlatData.size) {
                notifyItemRangeChanged(positionStart, removeCount)
            } else {
                notifyItemRangeRemoved(positionStart, removeCount)
                notifyItemRangeInserted(positionStart, newFlatData.size)
            }

        }
    }

    

    
    private fun flatData(list: Collection<BaseNode>, isExpanded: Boolean? = null): MutableList<BaseNode> {
        val newList = ArrayList<BaseNode>()

        for (element in list) {
            newList.add(element)

            if (element is BaseExpandNode) {
                
                if (isExpanded == true || element.isExpanded) {
                    val childNode = element.childNode
                    if (!childNode.isNullOrEmpty()) {
                        val items = flatData(childNode, isExpanded)
                        newList.addAll(items)
                    }
                }
                isExpanded?.let {
                    element.isExpanded = it
                }
            } else {
                val childNode = element.childNode
                if (!childNode.isNullOrEmpty()) {
                    val items = flatData(childNode, isExpanded)
                    newList.addAll(items)
                }
            }

            if (element is NodeFooterImp) {
                element.footerNode?.let {
                    newList.add(it)
                }
            }
        }
        return newList
    }


    
    private fun collapse(@IntRange(from = 0) position: Int,
                         isChangeChildCollapse: Boolean = false,
                         animate: Boolean = true,
                         notify: Boolean = true,
                         parentPayload: Any? = null): Int {
        val node = this.data[position]

        if (node is BaseExpandNode && node.isExpanded) {
            val adapterPosition = position + headerLayoutCount

            node.isExpanded = false
            if (node.childNode.isNullOrEmpty()) {
                notifyItemChanged(adapterPosition, parentPayload)
                return 0
            }
            val items = flatData(node.childNode!!, if (isChangeChildCollapse) false else null)
            val size = items.size
            this.data.removeAll(items)
            if (notify) {
                if (animate) {
                    notifyItemChanged(adapterPosition, parentPayload)
                    notifyItemRangeRemoved(adapterPosition + 1, size)
                } else {
                    notifyDataSetChanged()
                }
            }
            return size
        }
        return 0
    }

    
    private fun expand(@IntRange(from = 0) position: Int,
                       isChangeChildExpand: Boolean = false,
                       animate: Boolean = true,
                       notify: Boolean = true,
                       parentPayload: Any? = null): Int {
        val node = this.data[position]

        if (node is BaseExpandNode && !node.isExpanded) {
            val adapterPosition = position + headerLayoutCount

            node.isExpanded = true
            if (node.childNode.isNullOrEmpty()) {
                notifyItemChanged(adapterPosition, parentPayload)
                return 0
            }
            val items = flatData(node.childNode!!, if (isChangeChildExpand) true else null)
            val size = items.size
            this.data.addAll(position + 1, items)
            if (notify) {
                if (animate) {
                    notifyItemChanged(adapterPosition, parentPayload)
                    notifyItemRangeInserted(adapterPosition + 1, size)
                } else {
                    notifyDataSetChanged()
                }
            }
            return size
        }
        return 0
    }

    
    @JvmOverloads
    fun collapse(@IntRange(from = 0) position: Int,
                 animate: Boolean = true,
                 notify: Boolean = true,
                 parentPayload: Any? = null): Int {
        return collapse(position, false, animate, notify, parentPayload)
    }

    
    @JvmOverloads
    fun expand(@IntRange(from = 0) position: Int,
               animate: Boolean = true,
               notify: Boolean = true,
               parentPayload: Any? = null): Int {
        return expand(position, false, animate, notify, parentPayload)
    }

    
    @JvmOverloads
    fun expandOrCollapse(@IntRange(from = 0) position: Int,
                         animate: Boolean = true,
                         notify: Boolean = true,
                         parentPayload: Any? = null): Int {
        val node = this.data[position]
        if (node is BaseExpandNode) {
            return if (node.isExpanded) {
                collapse(position, false, animate, notify, parentPayload)
            } else {
                expand(position, false, animate, notify, parentPayload)
            }
        }
        return 0
    }

    @JvmOverloads
    fun expandAndChild(@IntRange(from = 0) position: Int,
                       animate: Boolean = true,
                       notify: Boolean = true,
                       parentPayload: Any? = null): Int {
        return expand(position, true, animate, notify, parentPayload)
    }

    @JvmOverloads
    fun collapseAndChild(@IntRange(from = 0) position: Int,
                         animate: Boolean = true,
                         notify: Boolean = true,
                         parentPayload: Any? = null): Int {
        return collapse(position, true, animate, notify, parentPayload)
    }

    
    @JvmOverloads
    fun expandAndCollapseOther(@IntRange(from = 0) position: Int,
                               isExpandedChild: Boolean = false,
                               isCollapseChild: Boolean = true,
                               animate: Boolean = true,
                               notify: Boolean = true,
                               expandPayload: Any? = null,
                               collapsePayload: Any? = null) {

        val expandCount = expand(position, isExpandedChild, animate, notify, expandPayload)
        if (expandCount == 0) {
            return
        }

        val parentPosition = findParentNode(position)
        
        val firstPosition: Int = if (parentPosition == -1) {
            0 
        } else {
            parentPosition + 1 
        }

        
        var newPosition = position

        
        val beforeAllSize = position - firstPosition
        
        if (beforeAllSize > 0) {
            
            var i = firstPosition
            do {
                val collapseSize = collapse(i, isCollapseChild, animate, notify, collapsePayload)
                i++
                
                newPosition -= collapseSize
            } while (i < newPosition)
        }

        
        var lastPosition: Int = if (parentPosition == -1) {
            data.size - 1 
        } else {
            val dataSize = data[parentPosition].childNode?.size ?: 0
            parentPosition + dataSize + expandCount 
        }

        
        if ((newPosition + expandCount) < lastPosition) {
            var i = newPosition + expandCount + 1
            while (i <= lastPosition) {
                val collapseSize = collapse(i, isCollapseChild, animate, notify, collapsePayload)
                i++
                lastPosition -= collapseSize
            }

        }

    }


    
    fun findParentNode(node: BaseNode): Int {
        val pos = this.data.indexOf(node)
        if (pos == -1 || pos == 0) {
            return -1
        }

        for (i in pos - 1 downTo 0) {
            val tempNode = this.data[i]
            if (tempNode.childNode?.contains(node) == true) {
                return i
            }
        }
        return -1
    }

    fun findParentNode(@IntRange(from = 0) position: Int): Int {
        if (position == 0) {
            return -1
        }
        val node = this.data[position]
        for (i in position - 1 downTo 0) {
            val tempNode = this.data[i]
            if (tempNode.childNode?.contains(node) == true) {
                return i
            }
        }
        return -1
    }
}
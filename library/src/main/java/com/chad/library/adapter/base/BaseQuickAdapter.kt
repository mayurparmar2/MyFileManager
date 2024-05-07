package com.chad.library.adapter.base

import android.animation.Animator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.ViewParent
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.IdRes
import androidx.annotation.IntRange
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.chad.library.adapter.base.animation.*
import com.chad.library.adapter.base.diff.BrvahAsyncDiffer
import com.chad.library.adapter.base.diff.BrvahAsyncDifferConfig
import com.chad.library.adapter.base.diff.BrvahListUpdateCallback
import com.chad.library.adapter.base.listener.*
import com.chad.library.adapter.base.module.*
import com.chad.library.adapter.base.util.getItemView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import java.lang.ref.WeakReference
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType
import java.util.*
import kotlin.collections.ArrayList


private interface BaseQuickAdapterModuleImp {
    
    fun addLoadMoreModule(baseQuickAdapter: BaseQuickAdapter<*, *>): BaseLoadMoreModule {
        return BaseLoadMoreModule(baseQuickAdapter)
    }

    
    fun addUpFetchModule(baseQuickAdapter: BaseQuickAdapter<*, *>): BaseUpFetchModule {
        return BaseUpFetchModule(baseQuickAdapter)
    }

    
    fun addDraggableModule(baseQuickAdapter: BaseQuickAdapter<*, *>): BaseDraggableModule {
        return BaseDraggableModule(baseQuickAdapter)
    }
}


abstract class BaseQuickAdapter<T, VH : BaseViewHolder>
@JvmOverloads constructor(@LayoutRes private val layoutResId: Int,
                          data: MutableList<T>? = null)
    : RecyclerView.Adapter<VH>(), BaseQuickAdapterModuleImp, BaseListenerImp {

    companion object {
        const val HEADER_VIEW = 0x10000111
        const val LOAD_MORE_VIEW = 0x10000222
        const val FOOTER_VIEW = 0x10000333
        const val EMPTY_VIEW = 0x10000555
    }

    
    
    var data: MutableList<T> = data ?: arrayListOf()
        internal set

    
    var headerWithEmptyEnable = false

    
    var footerWithEmptyEnable = false

    
    var isUseEmpty = true

    
    var headerViewAsFlow: Boolean = false
    var footerViewAsFlow: Boolean = false

    
    var animationEnable: Boolean = false

    
    var isAnimationFirstOnly = true

    
    var adapterAnimation: BaseAnimation? = null
        set(value) {
            animationEnable = true
            field = value
        }

    
    val loadMoreModule: BaseLoadMoreModule
        get() {
            checkNotNull(mLoadMoreModule) { "Please first implements LoadMoreModule" }
            return mLoadMoreModule!!
        }

    
    val upFetchModule: BaseUpFetchModule
        get() {
            checkNotNull(mUpFetchModule) { "Please first implements UpFetchModule" }
            return mUpFetchModule!!
        }

    
    val draggableModule: BaseDraggableModule
        get() {
            checkNotNull(mDraggableModule) { "Please first implements DraggableModule" }
            return mDraggableModule!!
        }

    
    private var mDiffHelper: BrvahAsyncDiffer<T>? = null

    private lateinit var mHeaderLayout: LinearLayout
    private lateinit var mFooterLayout: LinearLayout
    private lateinit var mEmptyLayout: FrameLayout

    private var mLastPosition = -1

    private var mSpanSizeLookup: GridSpanSizeLookup? = null
    private var mOnItemClickListener: OnItemClickListener? = null
    private var mOnItemLongClickListener: OnItemLongClickListener? = null
    private var mOnItemChildClickListener: OnItemChildClickListener? = null
    private var mOnItemChildLongClickListener: OnItemChildLongClickListener? = null
    private var mUpFetchModule: BaseUpFetchModule? = null
    private var mDraggableModule: BaseDraggableModule? = null
    internal var mLoadMoreModule: BaseLoadMoreModule? = null

    protected open lateinit var context: Context

    @Deprecated("Please use recyclerView", replaceWith = ReplaceWith("recyclerView"))
    lateinit var weakRecyclerView: WeakReference<RecyclerView>

    internal var mRecyclerView: RecyclerView? = null

    var recyclerView: RecyclerView
        set(value) {
            mRecyclerView = value
        }
        get() {
            checkNotNull(mRecyclerView) {
                "Please get it after onAttachedToRecyclerView()"
            }
            return mRecyclerView!!
        }

    

    init {
        checkModule()
    }

    
    private fun checkModule() {
        if (this is LoadMoreModule) {
            mLoadMoreModule = this.addLoadMoreModule(this)
        }
        if (this is UpFetchModule) {
            mUpFetchModule = this.addUpFetchModule(this)
        }
        if (this is DraggableModule) {
            mDraggableModule = this.addDraggableModule(this)
        }
    }

    
    protected abstract fun convert(holder: VH, item: T)

    
    protected open fun convert(holder: VH, item: T, payloads: List<Any>) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val baseViewHolder: VH
        when (viewType) {
            LOAD_MORE_VIEW -> {
                val view = mLoadMoreModule!!.loadMoreView.getRootView(parent)
                baseViewHolder = createBaseViewHolder(view)
                mLoadMoreModule!!.setupViewHolder(baseViewHolder)
            }
            HEADER_VIEW -> {
                val headerLayoutVp: ViewParent? = mHeaderLayout.parent
                if (headerLayoutVp is ViewGroup) {
                    headerLayoutVp.removeView(mHeaderLayout)
                }

                baseViewHolder = createBaseViewHolder(mHeaderLayout)
            }
            EMPTY_VIEW -> {
                val emptyLayoutVp: ViewParent? = mEmptyLayout.parent
                if (emptyLayoutVp is ViewGroup) {
                    emptyLayoutVp.removeView(mEmptyLayout)
                }

                baseViewHolder = createBaseViewHolder(mEmptyLayout)
            }
            FOOTER_VIEW -> {
                val footerLayoutVp: ViewParent? = mFooterLayout.parent
                if (footerLayoutVp is ViewGroup) {
                    footerLayoutVp.removeView(mFooterLayout)
                }

                baseViewHolder = createBaseViewHolder(mFooterLayout)
            }
            else -> {
                val viewHolder = onCreateDefViewHolder(parent, viewType)
                bindViewClickListener(viewHolder, viewType)
                mDraggableModule?.initView(viewHolder)
                onItemViewHolderCreated(viewHolder, viewType)
                baseViewHolder = viewHolder
            }
        }

        return baseViewHolder
    }

    
    override fun getItemCount(): Int {
        if (hasEmptyView()) {
            var count = 1
            if (headerWithEmptyEnable && hasHeaderLayout()) {
                count++
            }
            if (footerWithEmptyEnable && hasFooterLayout()) {
                count++
            }
            return count
        } else {
            val loadMoreCount = if (mLoadMoreModule?.hasLoadMoreView() == true) {
                1
            } else {
                0
            }
            return headerLayoutCount + getDefItemCount() + footerLayoutCount + loadMoreCount
        }
    }

    
    override fun getItemViewType(position: Int): Int {
        if (hasEmptyView()) {
            val header = headerWithEmptyEnable && hasHeaderLayout()
            return when (position) {
                0 -> if (header) {
                    HEADER_VIEW
                } else {
                    EMPTY_VIEW
                }
                1 -> if (header) {
                    EMPTY_VIEW
                } else {
                    FOOTER_VIEW
                }
                2 -> FOOTER_VIEW
                else -> EMPTY_VIEW
            }
        }

        val hasHeader = hasHeaderLayout()
        if (hasHeader && position == 0) {
            return HEADER_VIEW
        } else {
            var adjPosition = if (hasHeader) {
                position - 1
            } else {
                position
            }
            val dataSize = data.size
            return if (adjPosition < dataSize) {
                getDefItemViewType(adjPosition)
            } else {
                adjPosition -= dataSize
                val numFooters = if (hasFooterLayout()) {
                    1
                } else {
                    0
                }
                if (adjPosition < numFooters) {
                    FOOTER_VIEW
                } else {
                    LOAD_MORE_VIEW
                }
            }
        }
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        
        mUpFetchModule?.autoUpFetch(position)
        
        mLoadMoreModule?.autoLoadMore(position)
        when (holder.itemViewType) {
            LOAD_MORE_VIEW -> {
                mLoadMoreModule?.let {
                    it.loadMoreView.convert(holder, position, it.loadMoreStatus)
                }
            }
            HEADER_VIEW, EMPTY_VIEW, FOOTER_VIEW -> return
            else -> convert(holder, getItem(position - headerLayoutCount))
        }
    }

    override fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
            return
        }
        
        mUpFetchModule?.autoUpFetch(position)
        
        mLoadMoreModule?.autoLoadMore(position)
        when (holder.itemViewType) {
            LOAD_MORE_VIEW -> {
                mLoadMoreModule?.let {
                    it.loadMoreView.convert(holder, position, it.loadMoreStatus)
                }
            }
            HEADER_VIEW, EMPTY_VIEW, FOOTER_VIEW -> return
            else -> convert(holder, getItem(position - headerLayoutCount), payloads)
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    
    override fun onViewAttachedToWindow(holder: VH) {
        super.onViewAttachedToWindow(holder)
        val type = holder.itemViewType
        if (isFixedViewType(type)) {
            setFullSpan(holder)
        } else {
            addAnimation(holder)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        weakRecyclerView = WeakReference(recyclerView)
        mRecyclerView = recyclerView

        this.context = recyclerView.context
        mDraggableModule?.attachToRecyclerView(recyclerView)

        val manager = recyclerView.layoutManager
        if (manager is GridLayoutManager) {
            val defSpanSizeLookup = manager.spanSizeLookup
            manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val type = getItemViewType(position)
                    if (type == HEADER_VIEW && headerViewAsFlow) {
                        return 1
                    }
                    if (type == FOOTER_VIEW && footerViewAsFlow) {
                        return 1
                    }
                    return if (mSpanSizeLookup == null) {
                        if (isFixedViewType(type)) manager.spanCount else defSpanSizeLookup.getSpanSize(position)
                    } else {
                        if (isFixedViewType(type))
                            manager.spanCount
                        else
                            mSpanSizeLookup!!.getSpanSize(manager, type, position - headerLayoutCount)
                    }
                }

            }
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        mRecyclerView = null
    }

    protected open fun isFixedViewType(type: Int): Boolean {
        return type == EMPTY_VIEW || type == HEADER_VIEW || type == FOOTER_VIEW || type == LOAD_MORE_VIEW
    }

    
    open fun getItem(@IntRange(from = 0) position: Int): T {
        return data[position]
    }

    open fun getItemOrNull(@IntRange(from = 0) position: Int): T? {
        return data.getOrNull(position)
    }

    
    open fun getItemPosition(item: T?): Int {
        return if (item != null && data.isNotEmpty()) data.indexOf(item) else -1
    }

    
    private val childClickViewIds = LinkedHashSet<Int>()

    fun getChildClickViewIds(): LinkedHashSet<Int> {
        return childClickViewIds
    }

    
    fun addChildClickViewIds(@IdRes vararg viewIds: Int) {
        for (viewId in viewIds) {
            childClickViewIds.add(viewId)
        }
    }

    
    private val childLongClickViewIds = LinkedHashSet<Int>()

    fun getChildLongClickViewIds(): LinkedHashSet<Int> {
        return childLongClickViewIds
    }

    
    fun addChildLongClickViewIds(@IdRes vararg viewIds: Int) {
        for (viewId in viewIds) {
            childLongClickViewIds.add(viewId)
        }
    }

    
    protected open fun bindViewClickListener(viewHolder: VH, viewType: Int) {
        mOnItemClickListener?.let {
            viewHolder.itemView.setOnClickListener { v ->
                var position = viewHolder.adapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnClickListener
                }
                position -= headerLayoutCount
                setOnItemClick(v, position)
            }
        }
        mOnItemLongClickListener?.let {
            viewHolder.itemView.setOnLongClickListener { v ->
                var position = viewHolder.adapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnLongClickListener false
                }
                position -= headerLayoutCount
                setOnItemLongClick(v, position)
            }
        }

        mOnItemChildClickListener?.let {
            for (id in getChildClickViewIds()) {
                viewHolder.itemView.findViewById<View>(id)?.let { childView ->
                    if (!childView.isClickable) {
                        childView.isClickable = true
                    }
                    childView.setOnClickListener { v ->
                        var position = viewHolder.adapterPosition
                        if (position == RecyclerView.NO_POSITION) {
                            return@setOnClickListener
                        }
                        position -= headerLayoutCount
                        setOnItemChildClick(v, position)
                    }
                }
            }
        }
        mOnItemChildLongClickListener?.let {
            for (id in getChildLongClickViewIds()) {
                viewHolder.itemView.findViewById<View>(id)?.let { childView ->
                    if (!childView.isLongClickable) {
                        childView.isLongClickable = true
                    }
                    childView.setOnLongClickListener { v ->
                        var position = viewHolder.adapterPosition
                        if (position == RecyclerView.NO_POSITION) {
                            return@setOnLongClickListener false
                        }
                        position -= headerLayoutCount
                        setOnItemChildLongClick(v, position)
                    }
                }
            }
        }
    }

    
    protected open fun setOnItemClick(v: View, position: Int) {
        mOnItemClickListener?.onItemClick(this, v, position)
    }

    
    protected open fun setOnItemLongClick(v: View, position: Int): Boolean {
        return mOnItemLongClickListener?.onItemLongClick(this, v, position) ?: false
    }

    protected open fun setOnItemChildClick(v: View, position: Int) {
        mOnItemChildClickListener?.onItemChildClick(this, v, position)
    }

    protected open fun setOnItemChildLongClick(v: View, position: Int): Boolean {
        return mOnItemChildLongClickListener?.onItemChildLongClick(this, v, position) ?: false
    }

    
    protected open fun onItemViewHolderCreated(viewHolder: VH, viewType: Int) {}

    
    protected open fun getDefItemCount(): Int {
        return data.size
    }

    
    protected open fun getDefItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    
    protected open fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): VH {
        return createBaseViewHolder(parent, layoutResId)
    }

    protected open fun createBaseViewHolder(parent: ViewGroup, @LayoutRes layoutResId: Int): VH {
        return createBaseViewHolder(parent.getItemView(layoutResId))
    }

    
    @Suppress("UNCHECKED_CAST")
    protected open fun createBaseViewHolder(view: View): VH {
        var temp: Class<*>? = javaClass
        var z: Class<*>? = null
        while (z == null && null != temp) {
            z = getInstancedGenericKClass(temp)
            temp = temp.superclass
        }
        
        val vh: VH? = if (z == null) {
            BaseViewHolder(view) as VH
        } else {
            createBaseGenericKInstance(z, view)
        }
        return vh ?: BaseViewHolder(view) as VH
    }

    
    private fun getInstancedGenericKClass(z: Class<*>): Class<*>? {
        try {
            val type = z.genericSuperclass
            if (type is ParameterizedType) {
                val types = type.actualTypeArguments
                for (temp in types) {
                    if (temp is Class<*>) {
                        if (BaseViewHolder::class.java.isAssignableFrom(temp)) {
                            return temp
                        }
                    } else if (temp is ParameterizedType) {
                        val rawType = temp.rawType
                        if (rawType is Class<*> && BaseViewHolder::class.java.isAssignableFrom(rawType)) {
                            return rawType
                        }
                    }
                }
            }
        } catch (e: java.lang.reflect.GenericSignatureFormatError) {
            e.printStackTrace()
        } catch (e: TypeNotPresentException) {
            e.printStackTrace()
        } catch (e: java.lang.reflect.MalformedParameterizedTypeException) {
            e.printStackTrace()
        }
        return null
    }

    
    @Suppress("UNCHECKED_CAST")
    private fun createBaseGenericKInstance(z: Class<*>, view: View): VH? {
        try {
            val constructor: Constructor<*>
            
            return if (z.isMemberClass && !Modifier.isStatic(z.modifiers)) {
                constructor = z.getDeclaredConstructor(javaClass, View::class.java)
                constructor.isAccessible = true
                constructor.newInstance(this, view) as VH
            } else {
                constructor = z.getDeclaredConstructor(View::class.java)
                constructor.isAccessible = true
                constructor.newInstance(view) as VH
            }
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }

        return null
    }

    
    protected open fun setFullSpan(holder: RecyclerView.ViewHolder) {
        val layoutParams = holder.itemView.layoutParams
        if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            layoutParams.isFullSpan = true
        }
    }

    
    fun getViewByPosition(position: Int, @IdRes viewId: Int): View? {
        val recyclerView = mRecyclerView ?: return null
        val viewHolder = recyclerView.findViewHolderForLayoutPosition(position) as BaseViewHolder?
                ?: return null
        return viewHolder.getViewOrNull(viewId)
    }

    
    
    
    @JvmOverloads
    fun addHeaderView(view: View, index: Int = -1, orientation: Int = LinearLayout.VERTICAL): Int {
        if (!this::mHeaderLayout.isInitialized) {
            mHeaderLayout = LinearLayout(view.context)
            mHeaderLayout.orientation = orientation
            mHeaderLayout.layoutParams = if (orientation == LinearLayout.VERTICAL) {
                RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            } else {
                RecyclerView.LayoutParams(WRAP_CONTENT, MATCH_PARENT)
            }
        }

        val childCount = mHeaderLayout.childCount
        var mIndex = index
        if (index < 0 || index > childCount) {
            mIndex = childCount
        }
        mHeaderLayout.addView(view, mIndex)
        if (mHeaderLayout.childCount == 1) {
            val position = headerViewPosition
            if (position != -1) {
                notifyItemInserted(position)
            }
        }
        return mIndex
    }

    @JvmOverloads
    fun setHeaderView(view: View, index: Int = 0, orientation: Int = LinearLayout.VERTICAL): Int {
        return if (!this::mHeaderLayout.isInitialized || mHeaderLayout.childCount <= index) {
            addHeaderView(view, index, orientation)
        } else {
            mHeaderLayout.removeViewAt(index)
            mHeaderLayout.addView(view, index)
            index
        }
    }

    
    fun hasHeaderLayout(): Boolean {
        if (this::mHeaderLayout.isInitialized && mHeaderLayout.childCount > 0) {
            return true
        }
        return false
    }

    fun removeHeaderView(header: View) {
        if (!hasHeaderLayout()) return

        mHeaderLayout.removeView(header)
        if (mHeaderLayout.childCount == 0) {
            val position = headerViewPosition
            if (position != -1) {
                notifyItemRemoved(position)
            }
        }
    }

    fun removeAllHeaderView() {
        if (!hasHeaderLayout()) return

        mHeaderLayout.removeAllViews()
        val position = headerViewPosition
        if (position != -1) {
            notifyItemRemoved(position)
        }
    }

    val headerViewPosition: Int
        get() {
            if (hasEmptyView()) {
                if (headerWithEmptyEnable) {
                    return 0
                }
            } else {
                return 0
            }
            return -1
        }

    
    val headerLayoutCount: Int
        get() {
            return if (hasHeaderLayout()) {
                1
            } else {
                0
            }
        }


    
    val headerLayout: LinearLayout?
        get() {
            return if (this::mHeaderLayout.isInitialized) {
                mHeaderLayout
            } else {
                null
            }
        }

    
    
    
    @JvmOverloads
    fun addFooterView(view: View, index: Int = -1, orientation: Int = LinearLayout.VERTICAL): Int {
        if (!this::mFooterLayout.isInitialized) {
            mFooterLayout = LinearLayout(view.context)
            mFooterLayout.orientation = orientation
            mFooterLayout.layoutParams = if (orientation == LinearLayout.VERTICAL) {
                RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            } else {
                RecyclerView.LayoutParams(WRAP_CONTENT, MATCH_PARENT)
            }
        }

        val childCount = mFooterLayout.childCount
        var mIndex = index
        if (index < 0 || index > childCount) {
            mIndex = childCount
        }
        mFooterLayout.addView(view, mIndex)
        if (mFooterLayout.childCount == 1) {
            val position = footerViewPosition
            if (position != -1) {
                notifyItemInserted(position)
            }
        }
        return mIndex
    }

    @JvmOverloads
    fun setFooterView(view: View, index: Int = 0, orientation: Int = LinearLayout.VERTICAL): Int {
        return if (!this::mFooterLayout.isInitialized || mFooterLayout.childCount <= index) {
            addFooterView(view, index, orientation)
        } else {
            mFooterLayout.removeViewAt(index)
            mFooterLayout.addView(view, index)
            index
        }
    }

    fun removeFooterView(footer: View) {
        if (!hasFooterLayout()) return

        mFooterLayout.removeView(footer)
        if (mFooterLayout.childCount == 0) {
            val position = footerViewPosition
            if (position != -1) {
                notifyItemRemoved(position)
            }
        }
    }

    fun removeAllFooterView() {
        if (!hasFooterLayout()) return

        mFooterLayout.removeAllViews()
        val position = footerViewPosition
        if (position != -1) {
            notifyItemRemoved(position)
        }
    }

    fun hasFooterLayout(): Boolean {
        if (this::mFooterLayout.isInitialized && mFooterLayout.childCount > 0) {
            return true
        }
        return false
    }

    val footerViewPosition: Int
        get() {
            if (hasEmptyView()) {
                var position = 1
                if (headerWithEmptyEnable && hasHeaderLayout()) {
                    position++
                }
                if (footerWithEmptyEnable) {
                    return position
                }
            } else {
                return headerLayoutCount + data.size
            }
            return -1
        }

    
    val footerLayoutCount: Int
        get() {
            return if (hasFooterLayout()) {
                1
            } else {
                0
            }
        }

    
    val footerLayout: LinearLayout?
        get() {
            return if (this::mFooterLayout.isInitialized) {
                mFooterLayout
            } else {
                null
            }
        }

    
    
    
    
    fun setEmptyView(emptyView: View) {
        val oldItemCount = itemCount
        var insert = false
        if (!this::mEmptyLayout.isInitialized) {
            mEmptyLayout = FrameLayout(emptyView.context)

            mEmptyLayout.layoutParams = emptyView.layoutParams?.let {
                return@let ViewGroup.LayoutParams(it.width, it.height)
            } ?: ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)

            insert = true
        } else {
            emptyView.layoutParams?.let {
                val lp = mEmptyLayout.layoutParams
                lp.width = it.width
                lp.height = it.height
                mEmptyLayout.layoutParams = lp
            }
        }

        mEmptyLayout.removeAllViews()
        mEmptyLayout.addView(emptyView)
        isUseEmpty = true
        if (insert && hasEmptyView()) {
            var position = 0
            if (headerWithEmptyEnable && hasHeaderLayout()) {
                position++
            }
            if (itemCount > oldItemCount) {
                notifyItemInserted(position)
            } else {
                notifyDataSetChanged()
            }
        }
    }

    fun setEmptyView(layoutResId: Int) {
        mRecyclerView?.let {
            val view = LayoutInflater.from(it.context).inflate(layoutResId, it, false)
            setEmptyView(view)
        }
    }

    fun removeEmptyView() {
        if (this::mEmptyLayout.isInitialized) {
            mEmptyLayout.removeAllViews()
        }
    }

    fun hasEmptyView(): Boolean {
        if (!this::mEmptyLayout.isInitialized || mEmptyLayout.childCount == 0) {
            return false
        }
        if (!isUseEmpty) {
            return false
        }
        return data.isEmpty()
    }

    
    val emptyLayout: FrameLayout?
        get() {
            return if (this::mEmptyLayout.isInitialized) {
                mEmptyLayout
            } else {
                null
            }
        }


    

    
    private fun addAnimation(holder: RecyclerView.ViewHolder) {
        if (animationEnable) {
            if (!isAnimationFirstOnly || holder.layoutPosition > mLastPosition) {
                val animation: BaseAnimation = adapterAnimation ?: AlphaInAnimation()
                animation.animators(holder.itemView).forEach {
                    startAnim(it, holder.layoutPosition)
                }
                mLastPosition = holder.layoutPosition
            }
        }
    }

    
    protected open fun startAnim(anim: Animator, index: Int) {
        anim.start()
    }

    
    enum class AnimationType {
        AlphaIn, ScaleIn, SlideInBottom, SlideInLeft, SlideInRight
    }

    
    fun setAnimationWithDefault(animationType: AnimationType) {
        adapterAnimation = when (animationType) {
            AnimationType.AlphaIn -> AlphaInAnimation()
            AnimationType.ScaleIn -> ScaleInAnimation()
            AnimationType.SlideInBottom -> SlideInBottomAnimation()
            AnimationType.SlideInLeft -> SlideInLeftAnimation()
            AnimationType.SlideInRight -> SlideInRightAnimation()
        }
    }

    

    
    @Deprecated("Please use setNewInstance(), This method will be removed in the next version", replaceWith = ReplaceWith("setNewInstance(data)"))
    open fun setNewData(data: MutableList<T>?) {
        setNewInstance(data)
    }

    
    open fun setNewInstance(list: MutableList<T>?) {
        if (list === this.data) {
            return
        }

        this.data = list ?: arrayListOf()
        mLoadMoreModule?.reset()
        mLastPosition = -1
        notifyDataSetChanged()
        mLoadMoreModule?.checkDisableLoadMoreIfNotFullPage()
    }

    
    @Deprecated("Please use setData()", replaceWith = ReplaceWith("setData(newData)"))
    open fun replaceData(newData: Collection<T>) {
        setList(newData)
    }

    open fun setList(list: Collection<T>?) {
        if (list !== this.data) {
            this.data.clear()
            if (!list.isNullOrEmpty()) {
                this.data.addAll(list)
            }
        } else {
            if (!list.isNullOrEmpty()) {
                val newList = ArrayList(list)
                this.data.clear()
                this.data.addAll(newList)
            } else {
                this.data.clear()
            }
        }
        mLoadMoreModule?.reset()
        mLastPosition = -1
        notifyDataSetChanged()
        mLoadMoreModule?.checkDisableLoadMoreIfNotFullPage()
    }
//    open fun setList(list: ArrayList<Any>?) {
//        if (list !== this.data) {
//            this.data.clear()
//            if (!list.isNullOrEmpty()) {
//                this.data.addAll(list)
//            }
//        } else {
//            if (!list.isNullOrEmpty()) {
//                val newList = ArrayList(list)
//                this.data.clear()
//                this.data.addAll(newList)
//            } else {
//                this.data.clear()
//            }
//        }
//        mLoadMoreModule?.reset()
//        mLastPosition = -1
//        notifyDataSetChanged()
//        mLoadMoreModule?.checkDisableLoadMoreIfNotFullPage()
//    }

    
    open fun setData(@IntRange(from = 0) index: Int, data: T) {
        if (index >= this.data.size) {
            return
        }
        this.data[index] = data
        notifyItemChanged(index + headerLayoutCount)
    }

    
    open fun addData(@IntRange(from = 0) position: Int, data: T) {
        this.data.add(position, data)
        notifyItemInserted(position + headerLayoutCount)
        compatibilityDataSizeChanged(1)
    }

    
    open fun addData(@NonNull data: T) {
        this.data.add(data)
        notifyItemInserted(this.data.size + headerLayoutCount)
        compatibilityDataSizeChanged(1)
    }

    
    open fun addData(@IntRange(from = 0) position: Int, newData: Collection<T>) {
        this.data.addAll(position, newData)
        notifyItemRangeInserted(position + headerLayoutCount, newData.size)
        compatibilityDataSizeChanged(newData.size)
    }

    open fun addData(@NonNull newData: Collection<T>) {
        this.data.addAll(newData)
        notifyItemRangeInserted(this.data.size - newData.size + headerLayoutCount, newData.size)
        compatibilityDataSizeChanged(newData.size)
    }

    
    @Deprecated("Please use removeAt()", replaceWith = ReplaceWith("removeAt(position)"))
    open fun remove(@IntRange(from = 0) position: Int) {
        removeAt(position)
    }

    
    open fun removeAt(@IntRange(from = 0) position: Int) {
        if (position >= data.size) {
            return
        }
        this.data.removeAt(position)
        val internalPosition = position + headerLayoutCount
        notifyItemRemoved(internalPosition)
        compatibilityDataSizeChanged(0)
        notifyItemRangeChanged(internalPosition, this.data.size - internalPosition)
    }

    open fun remove(data: T) {
        val index = this.data.indexOf(data)
        if (index == -1) {
            return
        }
        removeAt(index)
    }


    
    protected fun compatibilityDataSizeChanged(size: Int) {
        if (this.data.size == size) {
            notifyDataSetChanged()
        }
    }

    
    fun setDiffCallback(diffCallback: DiffUtil.ItemCallback<T>) {
        this.setDiffConfig(BrvahAsyncDifferConfig.Builder(diffCallback).build())
    }

    
    fun setDiffConfig(config: BrvahAsyncDifferConfig<T>) {
        mDiffHelper = BrvahAsyncDiffer(this, config)
    }

    @Deprecated("User getDiffer()", replaceWith = ReplaceWith("getDiffer()"))
    fun getDiffHelper(): BrvahAsyncDiffer<T> {
        return getDiffer()
    }

    fun getDiffer(): BrvahAsyncDiffer<T> {
        checkNotNull(mDiffHelper) {
            "Please use setDiffCallback() or setDiffConfig() first!"
        }
        return mDiffHelper!!
    }

    
    open fun setDiffNewData(list: MutableList<T>?) {
        if (hasEmptyView()) {
            
            setNewInstance(list)
            return
        }
        mDiffHelper?.submitList(list)
    }

    
    open fun setDiffNewData(@NonNull diffResult: DiffUtil.DiffResult, list: MutableList<T>) {
        if (hasEmptyView()) {
            
            setNewInstance(list)
            return
        }
        diffResult.dispatchUpdatesTo(BrvahListUpdateCallback(this))
        this.data = list
    }

    

    override fun setGridSpanSizeLookup(spanSizeLookup: GridSpanSizeLookup?) {
        this.mSpanSizeLookup = spanSizeLookup
    }

    override fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.mOnItemClickListener = listener
    }

    override fun setOnItemLongClickListener(listener: OnItemLongClickListener?) {
        this.mOnItemLongClickListener = listener
    }

    override fun setOnItemChildClickListener(listener: OnItemChildClickListener?) {
        this.mOnItemChildClickListener = listener
    }

    override fun setOnItemChildLongClickListener(listener: OnItemChildLongClickListener?) {
        this.mOnItemChildLongClickListener = listener
    }

    fun getOnItemClickListener(): OnItemClickListener? = mOnItemClickListener

    fun getOnItemLongClickListener(): OnItemLongClickListener? = mOnItemLongClickListener

    fun getOnItemChildClickListener(): OnItemChildClickListener? = mOnItemChildClickListener

    fun getOnItemChildLongClickListener(): OnItemChildLongClickListener? = mOnItemChildLongClickListener
}

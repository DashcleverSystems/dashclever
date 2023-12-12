package pl.dashclever.commons.paging

data class PagingInfo<T>(
    val totalElements: Long,
    val pageNumber: Int,
    val totalPages: Int,
    val content: List<T>
)

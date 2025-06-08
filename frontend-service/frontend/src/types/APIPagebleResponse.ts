export interface APIPagebleResponse<T> {
    content: T[];
    totalPages: number;
    totalElements: number;
}
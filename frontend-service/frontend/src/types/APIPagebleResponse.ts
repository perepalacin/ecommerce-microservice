export interface APIPagebleResponse<T> {
    content: T[];
    totalPages: number;
    totalElements: number;
    number: number;
    size: number;   
}
export interface NewsDTO {
    id?: number,
    authorUsername: string | undefined,
    creationTime: string,
    newsHeader: string,
    newsText: string
}
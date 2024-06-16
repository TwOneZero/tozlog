import Post from "@/entity/post/Post";
import type PostWrite from "@/entity/post/PostWrite";
import { plainToInstance } from "class-transformer";
import { inject, singleton } from "tsyringe";
import HttpRepository from "./HttpRepository";
import Paging from "@/entity/data/Paging";

@singleton()
class PostRepository {
    constructor(@inject(HttpRepository) private readonly httpRepository: HttpRepository) {}

    public write = (request: PostWrite) => {
        return this.httpRepository.post({
            path: "/api/posts",
            body: request,
        });
    };

    public get = async (postId: number): Promise<Post> => {
        return this.httpRepository.get<Post>({ path: `/api/posts/${postId}` }, Post);
    };
    public getList = async (page: number): Promise<Paging<Post>> => {
        return this.httpRepository.getWithPaged<Post>(
            { path: `/api/posts?page=${page}&size=3` },
            Post,
        );
    };
    public delete = () => {};
}

export default PostRepository;

import Post from "@/entity/post/Post";
import type PostWrite from "@/entity/post/PostWrite";
import {plainToInstance} from "class-transformer";
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

    public get = async (postId: number) => {
        return this.httpRepository.get({ path: `/api/posts/${postId}` }).then((response) => {
            return plainToInstance(Post, response);
        });
    };
    public getList = async (page: number) => {
        return this.httpRepository.get({ path: `/api/posts?page=${page}&size=3` }).then((response) => {
            const paging = plainToInstance<Paging<Post>, any>(Paging, response);
            const items = plainToInstance<Post, any[]>(Post, response.items);
            paging.setItems(items);
            return paging;
        });
    };
    public delete = () => {};
}

export default PostRepository;

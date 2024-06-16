import type Login from "@/entity/user/Login";
import HttpRepository from "./HttpRepository";
import { inject, singleton } from "tsyringe";
import UserProfile from "@/entity/user/UserProfile";

@singleton()
class UserRepository {
    constructor(@inject(HttpRepository) private readonly httpRepository: HttpRepository) {}
    public login = (request: Login) => {
        return this.httpRepository.post({
            path: "/api/auth/login",
            body: request,
        });
    };

    public getProfile = async () => {
        return this.httpRepository.get<UserProfile>(
            {
                path: "/api/users/me",
            },
            UserProfile,
        );
    };
}

export default UserRepository;

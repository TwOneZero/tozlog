import type { AxiosInstance, AxiosResponse } from "axios";
import axios, { AxiosError } from "axios";
import HttpError from "@/http/HttpError";
import { singleton } from "tsyringe";

export type HttpRequestConfig = {
    path: string;
    method?: "GET" | "POST" | "PUT" | "PATCH" | "DELETE";
    params?: any;
    body?: any;
};

@singleton()
class AxiosHttpClient {
    private readonly client: AxiosInstance = axios.create({
        timeout: 3000,
        timeoutErrorMessage: "네트워크 상태가 좋지 않습니다 :(",
    });

    public request = async (config: HttpRequestConfig) => {
        return this.client
            .request({
                url: config.path,
                method: config.method,
                params: config.params,
                data: config.body,
            })
            .then((response: AxiosResponse) => {
                return response.data;
            })
            .catch((e: AxiosError) => {
                return Promise.reject(new HttpError(e));
            });
    };
}

export default AxiosHttpClient;

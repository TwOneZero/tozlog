import type { HttpRequestConfig } from "@/http/AxiosHttpClient";
import AxiosHttpClient from "@/http/AxiosHttpClient";
import { inject, singleton } from "tsyringe";

@singleton()
class HttpRepository {
    constructor(@inject(AxiosHttpClient) private readonly httpClient: AxiosHttpClient) {
        this.httpClient = httpClient;
    }

    public get = async (config: HttpRequestConfig) => {
        return this.httpClient.request({ ...config, method: "GET" });
    };
    public post = async (config: HttpRequestConfig) => {
        return this.httpClient.request({ ...config, method: "POST" });
    };
    public put = async (config: HttpRequestConfig) => {
        return this.httpClient.request({ ...config, method: "PUT" });
    };
    public patch = async (config: HttpRequestConfig) => {
        return this.httpClient.request({ ...config, method: "PATCH" });
    };
    public delete = async (config: HttpRequestConfig) => {
        return this.httpClient.request({ ...config, method: "DELETE" });
    };
}

export default HttpRepository;

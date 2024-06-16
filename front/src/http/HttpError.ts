import type { AxiosError } from "axios";

class HttpError {
    private readonly _code: string;
    private readonly _message: string;

    constructor(axiosError: AxiosError<any, any>) {
        this._code = axiosError.response?.data.code ?? "500";
        this._message = axiosError.response?.data._message ?? "네트워크 상태가 안좋아요 :(";
    }

    get code(): string {
        return this._code;
    }
    get message(): string {
        return this._message;
    }
}

export default HttpError;

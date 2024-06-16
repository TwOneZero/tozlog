<script setup lang="ts">
import Login from "@/entity/user/Login";
import { reactive } from "vue";
import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";
import type HttpError from "@/http/HttpError";
import UserRepository from "@/repository/UserRepository";
import { container } from "tsyringe";

const router = useRouter();
const state = reactive({
    login: new Login(),
});

const USER_REPOSITORY = container.resolve(UserRepository);

const doLogin = () => {
    // const httpClient = new AxiosHttpClient();

    USER_REPOSITORY.login(state.login)
        .then(() => {
            ElMessage({ type: "success", message: "환영합니다 :)🤞" });
            // router.replace("/");
          // 로그아웃 시 재 렌더링을 위함
          location.href = "/";
        })
        .catch((e: HttpError) => {
            ElMessage({ type: "error", message: e.message });
        });
};
</script>

<template>
    <el-row>
        <el-col :span="10" :offset="7">
            <el-form label-position="top">
                <el-form-item label="이메일">
                    <el-input v-model="state.login.email"></el-input>
                </el-form-item>

                <el-form-item label="비밀번호">
                    <el-input type="password" v-model="state.login.password"></el-input>
                </el-form-item>

                <el-form-item>
                    <el-button type="primary" style="width: 100%" @click="doLogin()"
                        >로그인
                    </el-button>
                </el-form-item>
            </el-form>
        </el-col>
    </el-row>
</template>

<style scoped lang="scss"></style>

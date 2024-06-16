<script setup lang="ts">
import { onBeforeMount, reactive } from "vue";
import { container } from "tsyringe";
import UserRepository from "@/repository/UserRepository";
import UserProfile from "@/entity/user/UserProfile";
import ProfileRepository from "@/repository/ProfileRepository";
import {useRouter} from "vue-router";

const router = useRouter();

const USER_REPOSITORY = container.resolve(UserRepository);
const PROFILE_REPOSITORY = container.resolve(ProfileRepository);

type StateType = {
    profile: UserProfile | null;
};

const state = reactive<StateType>({
    profile: null,
});
onBeforeMount(() => {
    USER_REPOSITORY.getProfile().then((profile) => {
        PROFILE_REPOSITORY.setProfile(profile);
        state.profile = profile;
    });
});


const logout = () => {
  PROFILE_REPOSITORY.clear();
  location.href = "/api/logout";
}

</script>

<template>
    <ul class="menus">
        <li class="menu">
            <router-link to="/">처음으로</router-link>
        </li>

        <li class="menu" v-if="state.profile !== null">
            <router-link to="/write">글 작성</router-link>
        </li>

        <li class="menu" v-if="state.profile === null">
            <router-link to="/login">로그인</router-link>
        </li>
        <li class="menu" v-else>
            <a href="#" @click="logout()">{{state.profile!.name}}>로그아웃</a>
        </li>
    </ul>
</template>

<style scoped lang="scss">
.menus {
    height: 20px;
    list-style: none;
    padding: 0;
    font-size: 0.88rem;
    font-weight: 300;
    text-align: center;
    margin: 0;
}

.menu {
    display: inline;
    margin-right: 1rem;

    &:last-child {
        margin-right: 0;
    }

    a {
        color: inherit;
    }
}
</style>

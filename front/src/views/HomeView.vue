<script setup lang="ts">
import axios from "axios";
import { ref } from "vue";

// const posts = [
//   {id: 1, title: "제목1", content: "내용1"},
//   {id: 3, title: "제목3", content: "내용3"},
//   {id: 2, title: "제목2", content: "내용2"},
//   {id: 4, title: "제목4", content: "내용4"}
// ]

const posts = ref<any>([]);

const getPosts = () => {
    axios.get("/api/posts?page=1&size=5").then((response) => {
        response.data.forEach((post: any) => posts.value.push(post));
    });
};
getPosts();
</script>

<template>
    <ul>
        <li v-for="post in posts" :key="post.id">
            <div class="title">
                <router-link :to="{ name: 'read', params: { postId: post.postId } }">{{
                    post.title
                }}</router-link>
            </div>
            <div class="content">
                {{ post.content }}
            </div>

            <div class="sub d-flex">
                <div class="category">개발</div>
                <div class="regDate">2024-06-07</div>
            </div>
        </li>
    </ul>
</template>

<style scoped lang="scss">
ul {
    list-style: none;
    padding: 0;

    li {
        margin-bottom: 2rem;
        .title {
            font-size: 1.2rem;

            &:hover {
                text-decoration: underline;
            }
        }
        .content {
            font-size: 0.8rem;
            color: #777777;
            margin-top: 8px;
        }
        .sub {
            margin-top: 8px;
            font-size: 0.78rem;

            .regDate {
                color: #747474;
                margin-left: 10px;
            }
        }

        &:last-child {
            margin-bottom: 0;
        }
    }
}
</style>

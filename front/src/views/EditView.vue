<script setup lang="ts">
import axios from "axios";
import { ref } from "vue";
import { useRouter } from "vue-router";
const router = useRouter();
const props = defineProps({
    postId: {
        type: [Number, String],
        required: true,
    },
});

const post = ref({
    postId : 0,
    title: "",
    content: "",
})

axios.get(`/api/posts/${props.postId}`).then((response) => {
    post.value = response.data;
});

const edit = () => {
    axios
        .patch(`/api/posts/${props.postId}`, post.value)
        .then(() => {
            router.replace({ name: "home" });
        });
};
</script>

<template>
    <div>
        <el-input v-model="post.title" type="text" />
    </div>
    <div class="mt-2">
        <el-input v-model="post.content" type="textarea" rows="15"></el-input>
    </div>
    <div class="mt-2">
        <el-button type="warning" @click="edit()">글 수정 완료</el-button>
    </div>
</template>

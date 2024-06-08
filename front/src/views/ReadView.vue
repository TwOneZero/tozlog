<script setup lang="ts">
import router from "@/router";
import axios from "axios";
import { onMounted, ref } from "vue";

const props = defineProps({
    postId: {
        type: [Number, String],
        required: true,
    },
});

const post = ref({
    postId: 0,
    title: "",
    content: "",
});

const moveToEdit = () => {
    router.push({ name: "edit", params: { postId: props.postId } });
};

onMounted(() => {
    axios.get(`/api/posts/${props.postId}`).then((response) => {
        post.value = response.data;
    });
});
</script>

<template>
    <el-row>
        <el-col>
            <h2 class="title">{{ post.title }}</h2>
            <div class="sub d-flex">
                <div class="category">개발</div>
                <div class="regDate">2024-06-07</div>
            </div>
        </el-col>
    </el-row>
    <el-row class="mt-3">
        <el-col>
            <div class="content">{{ post.content }}</div>
        </el-col>
    </el-row>

    <el-row>
        <el-col>
            <div class="d-flex justify-content-end">
                <el-button class="btn-submit" type="warning" @click="moveToEdit()"
                    >글 수정</el-button
                >
            </div>
        </el-col>
    </el-row>
</template>

<style scoped lang="scss">
.title {
    font-size: 1.6rem;
    font-weight: 600;
    color: #383838;
    margin: 1px;
}
.content {
    font-size: 0.95rem;
    color: #464545;
    margin-top: 10px;
    white-space: break-spaces;
    line-height: 1.5;
}
.btn-submit {
    margin-top: 8px;
}

.sub {
    margin-top: 10px;
    font-size: 0.78rem;
    color: #545353;
    .category {
        font-weight: 600;
    }

    .regDate {
        color: #747474;
        margin-left: 10px;
    }
}
</style>

<script setup lang="ts">
import Comments from "@/components/Comments.vue";
import Post from "@/entity/post/Post";
import PostRepository from "@/repository/PostRepository";
import { container } from "tsyringe";
import { onMounted, reactive } from "vue";

const props = defineProps<{
    postId: string;
}>();

const POST_REPOSITORY = container.resolve(PostRepository);

type StateType = {
    post: Post;
};

const state = reactive<StateType>({
    post: new Post(),
});

const getPost = () => {
    POST_REPOSITORY.get(parseInt(props.postId))
        .then((post: Post) => {
            state.post = post;
        })
        .catch((e) => {
            console.log(e);
        });
};

const moveToEdit = () => {};

onMounted(() => {
    getPost();
});
</script>

<template>
    <el-row>
        <el-col>
            <h2 class="title">{{ state.post.title }}</h2>
            <div class="sub d-flex">
                <div class="category">개발</div>
                <div class="regDate">Posted on {{ state.post.getRegDateFormatted() }}</div>
            </div>
        </el-col>
    </el-row>
    <el-row class="mt-3">
        <el-col>
            <div class="content">{{ state.post.content }}</div>
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

    <el-row class="comments">
        <el-col>
            <Comments />
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

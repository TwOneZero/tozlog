<script setup lang="ts">
import PostComponent from "@/components/PostComponent.vue";
import Post from "@/entity/post/Post";
import PostRepository from "@/repository/PostRepository";
import { container } from "tsyringe";
import { computed, onMounted, reactive } from "vue";
import Paging from "@/entity/data/Paging";

const POST_REPOSITORY = container.resolve(PostRepository);

type StateType = {
    postList: Paging<Post>;
};
const state = reactive<StateType>({
    postList: new Paging<Post>(),
});

const getList = (page = 1) => {
    POST_REPOSITORY.getList(page).then((response) => {
        state.postList = response;
    });
};

onMounted(() => {
    getList();
});
</script>

<template>
    <div class="content">
        <span class="totalCount">게시글 수 : {{ state.postList.totalCount }}</span>
        <ul class="posts">
            <li v-for="post in state.postList.items as Post[]" :key="post.postId">
                <PostComponent :post="post" />
            </li>
        </ul>
        <div class="d-flex justify-content-center">
            <el-pagination
                :default-page-size="3"
                :total="state.postList.totalCount"
                @current-change="(page) => getList(page)"
                layout="prev, pager, next"
                background
                v-model:current-page="state.postList.page"
            />
        </div>
    </div>
</template>

<style scoped lang="scss">
.content {
    padding: 0 1rem 0 1rem;
    margin-bottom: 2rem;
}

.totalCount {
    padding: 0 0.2rem 0 0.2rem;
    font-size: 0.9rem;
}

.posts {
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

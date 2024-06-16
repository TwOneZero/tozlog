<script setup lang="ts">
import Post from "@/entity/post/Post";

const props = defineProps<{
    post: Post;
}>();
</script>

<template>
    <div class="title">
        <router-link :to="{ name: 'read', params: { postId: props.post.postId } }">{{
            post.title
        }}</router-link>
    </div>
    <div class="regDate">{{ props.post.getRegDateFormatted() }}</div>

    <div class="content">
        {{ props.post.content.slice(0, 100) }}...
    </div>

    <div class="readMore">
        <router-link :to="{ name: 'read', params: { postId: props.post.postId } }"
            >Read more</router-link
        >
    </div>
</template>

<style scoped lang="scss">
.title {
    a {
        color: inherit;
        text-decoration: none;
        font-size: 1.4rem;
        font-weight: 400;

        &:hover {
            text-decoration: underline;
        }
    }
}

.regDate {
    padding-left: 0.2rem;
    margin-top: 0.5rem;
    font-size: 0.78rem;
    font-weight: 300;
}

.content {
    padding: 0.2rem;
    margin-top: 0.88rem;
    font-weight: 300;
}

.readMore {
    margin-top: 0.3rem;

    a {
        font-size: 0.88rem;
        color: #3d8dcb;
        text-decoration: none;

        &:hover {
            text-decoration: underline;
        }
    }
}
</style>

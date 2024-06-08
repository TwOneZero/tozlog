<template>
    <el-header class="header">
        <!-- <RouterLink to="/">Home</RouterLink>
        <RouterLink to="/write">글 작성</RouterLink> -->
        <el-menu class="el-menu-demo" mode="horizontal">
            <el-menu-item index="/">
                <RouterLink to="/" class="full-link">Home</RouterLink>
            </el-menu-item>
            <el-menu-item index="/write">
                <RouterLink to="/write" class="full-link">글 작성</RouterLink>
            </el-menu-item>
        </el-menu>
    </el-header>
</template>

<script setup lang="ts"></script>

<style scoped>
.header {
    margin-bottom: 20px;
    padding: 0;
}

.el-menu-item {
    width: 84px;
    text-align: center;
    position: relative; /* 부모 요소를 기준으로 절대 위치 지정 */
    padding: 0; /* padding 제거 */
}

.full-link {
    display: flex;
    justify-content: center; /* 수평 중앙 정렬 */
    align-items: center; /* 수직 중앙 정렬 */
    width: 100%;
    height: 100%;
    position: absolute;
    top: 0;
    left: 0;
}
</style>

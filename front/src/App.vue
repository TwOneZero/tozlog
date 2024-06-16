<script setup lang="ts">
import MyHeader from "@/components/MyHeader.vue";
import Content from "@/components/Content.vue";
import Menu from "@/components/Menu.vue";
</script>

<template>
    <el-row>
        <el-col :span="22" :offset="1">
            <MyHeader />
        </el-col>
    </el-row>
    <el-row>
        <el-col :span="22" :offset="1">
            <Menu />
        </el-col>
    </el-row>
    <el-row>
        <el-col
            :span="22"
            :offset="1"
            :sm="{ span: 20, offset: 2 }"
            :md="{ span: 16, offset: 4 }"
            :lg="{ span: 14, offset: 5 }"
        >
            <Content />
        </el-col>
    </el-row>
</template>

<style scoped></style>

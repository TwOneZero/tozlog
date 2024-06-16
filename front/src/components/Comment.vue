<script setup lang="ts"></script>

<template>
    <div class="comment">
        <div class="header">
            <div class="section">
                <div class="author">일공이</div>
                <div class="regDate">2024-06-14 12:00:00</div>
            </div>

            <div class="delete">삭제</div>
        </div>
        <div class="content">나 이공일인데 개추했다.</div>
    </div>
</template>

<style scoped lang="scss">
.comment {
    width: 100%;
}

.header {
    display: flex;
    justify-content: space-between;
}

.section {
    display: flex;
    flex-direction: column;
}

.author {
    font-weight: 600;
    font-size: 1.2rem;
}

.regDate {
    margin-top: 5px;
    color: #797979;
    font-size: 0.88rem;
}

.content {
    margin-top: 0.8rem;
}

.delete {
    font-size: 0.78rem;
    color: red;
}
</style>

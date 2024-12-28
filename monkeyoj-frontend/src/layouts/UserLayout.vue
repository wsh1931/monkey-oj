<template>
  <div id="userLayout">
    <a-layout style="min-height: 100vh">
      <a-layout-header class="header">
        <a-space
          ><img src="../assets/logo.jpg" alt="" class="logo" />
          <div>猴 OJ</div>
        </a-space>
      </a-layout-header>
      <a-layout-content class="content">
        <router-view />
      </a-layout-content>
      <a-layout-footer class="footer">
        <a href="https://github.com/wsh1931">花果山OJ</a>
      </a-layout-footer>
    </a-layout>
  </div>
</template>

<style scoped>
.logo {
  width: 64px;
  height: 64px;
}

#userLayout {
  text-align: center;
  background-image: linear-gradient(120deg, #a1c4fd 0%, #c2e9fb 100%);
}

.header {
  box-shadow: aqua 1px 1px 5px;
}

.content {
  margin-bottom: 16px;
  padding: 20px;
}

.footer {
  padding: 16px;
  position: sticky;
  bottom: 0;
  left: 0;
  right: 0;
  text-align: center;
  color: white;
}
</style>
<script setup lang="ts"></script>

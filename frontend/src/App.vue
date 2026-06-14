<script setup lang="ts">
import { RouterView } from 'vue-router'
</script>

<template>
  <q-layout view="lHh Lpr lFf">
    
    <q-page-container>
      <router-view />
    </q-page-container>

  </q-layout>
</template>


<style>
body {
  background-color: #f5f5f5;
}
</style>
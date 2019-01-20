<template>
  <transition name="fade">
    <slot></slot>
  </transition>
</template>

<script>
export default {
  name: 'FormOverlay'
}
</script>

<style scoped>
  .center {
    margin: 0;
    position: absolute;
    top: 50%;
    left: 50%;
    -ms-transform: translate(-50%, -50%);
    transform: translate(-50%, -50%);
  }

  .fade-enter-active {
    animation: fade-in .7s;
  }
  .fade-leave-active {
    animation: fade-in .7s reverse;
  }
  @keyframes fade-in {
    0% {
      background: rgba(139, 139, 139, 0.0);
    }
    50% {
      background: rgba(139, 139, 139, 0.7);
    }
    100% {
      background: rgba(139, 139, 139, 0.5);
    }
  }
</style>

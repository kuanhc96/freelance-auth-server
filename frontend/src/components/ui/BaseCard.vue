<template>
    <div class="d-flex justify-content-center">
        <div class="card base-card bg-light col-md-10 mt-7 rounded-4">
            <div class="card-body">
                <div class="d-flex justify-content-between">
                    <h2 class="card-title">{{ cardTitle }}</h2>
                    <div class="refresh">
                        <button class="btn btn-primary" @click="refresh">Refresh</button>
                    </div>
                </div>
                <slot></slot>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import {defineComponent, PropType} from 'vue';

export default defineComponent({
    name: 'BaseCard',
    props: {
        cardTitle: {
            type: String as PropType<string>,
            required: true,
        }
    },
    setup(_, context) {
        function refresh() {
            context.emit('refresh');
        }

        return {
            refresh
        }
    },
})
</script>

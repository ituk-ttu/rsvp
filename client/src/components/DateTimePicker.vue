<template>
    <div class="dtp">
      <timepicker class="time" :minute-interval="5" v-model="timeData"></timepicker>
      <br>
      <datepicker class="date" :format="customFormatter" v-model="dateData" placeholder="dd.mm.yyyy" :inline="true"></datepicker>
      {{timeData}}
    </div>
</template>

<script>
import Datepicker from 'vuejs-datepicker';
import TimePicker from '@/components/TimePicker'
import moment from 'moment'

export default {
  name: 'DateTimePicker',
  components: {
    datepicker: Datepicker,
    timepicker: TimePicker
  },
  data: function () {
    return {
      timeData: {
        HH: '',
        mm: ''
      },
      dateData: ''
    }
  },
  computed: {
    timestamp: function () {
      if (this.timeData.HH === '' || this.timeData.mm === '' || this.dateData === '') {
        return null
      }

      return moment(this.dateData).add(this.timeData.HH, 'h').add(this.timeData.mm, 'm').unix()
    }
  },
  methods: {
    customFormatter(date) {
      return moment(date).format('DD.MM.YYYY');
    },
    close() {
      this.$emit('close');
    }
  }
}
</script>

<style>
  .time {
    bottom: 27px;
    left: 170px;
  }

  .date {
    display: inline-block;
  }

  .modal-backdrop {
    position: fixed;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    background-color: rgba(0, 0, 0, 0.3);
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .modal {
    background: #FFFFFF;
    box-shadow: 2px 2px 20px 1px;
    overflow-x: auto;
    display: flex;
    flex-direction: column;
  }

  .modal-header,
  .modal-footer {
    padding: 15px;
    display: flex;
  }

  .modal-header {
    border-bottom: 1px solid #eeeeee;
    color: #4AAE9B;
    justify-content: space-between;
  }

  .modal-footer {
    border-top: 1px solid #eeeeee;
    justify-content: flex-end;
  }

  .modal-body {
    position: relative;
    padding: 20px 10px;
  }

  .btn-close {
    border: none;
    font-size: 20px;
    padding: 20px;
    cursor: pointer;
    font-weight: bold;
    color: #4AAE9B;
    background: transparent;
  }

  .btn-green {
    color: white;
    background: #4AAE9B;
    border: 1px solid #4AAE9B;
    border-radius: 2px;
  }
</style>

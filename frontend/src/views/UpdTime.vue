<template>
  <q-page padding>
    <q-card flat bordered class="q-pa-md">
      <q-card-section>
        <div class="text-h6 text-center">重新安排約訪時間</div>
      </q-card-section>

      <q-card-section>
        <div class="sub-title">尚未完成約訪名單如下：</div>
        <div v-if="searchErrorMsg" class="error-text">{{ searchErrorMsg }}</div>

        <div class="row items-center q-gutter-sm">
          <span>查詢日期：</span>
          
          <q-input filled v-model="startTime" mask="####-##-## ##:##:##" placeholder="YYYY-MM-DD HH:mm:ss" dense class="datetime-input">
            <template v-slot:append>
              <q-icon name="event" class="cursor-pointer">
                <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                  <div class="row items-center justify-center q-gutter-sm q-pa-md bg-white">
                    <q-date v-model="startTime" mask="YYYY-MM-DD HH:mm:ss" color="positive" />
                    <q-time v-model="startTime" mask="YYYY-MM-DD HH:mm:ss" with-seconds color="positive" format24h />
                  </div>
                </q-popup-proxy>
              </q-icon>
            </template>
          </q-input>

          <span> ~ </span>

          <q-input filled v-model="endTime" mask="####-##-## ##:##:##" placeholder="YYYY-MM-DD HH:mm:ss" dense class="datetime-input">
            <template v-slot:append>
              <q-icon name="event" class="cursor-pointer">
                <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                  <div class="row items-center justify-center q-gutter-sm q-pa-md bg-white">
                    <q-date v-model="endTime" mask="YYYY-MM-DD HH:mm:ss" color="positive" />
                    <q-time v-model="endTime" mask="YYYY-MM-DD HH:mm:ss" with-seconds color="positive" format24h />
                  </div>
                </q-popup-proxy>
              </q-icon>
            </template>
          </q-input>

          <q-btn color="positive" label="查詢" @click="doSearch" />
        </div>
      </q-card-section>

      <q-card-section>
        <div class="row justify-between items-center q-mb-sm">
          <div>查詢結果：<span class="text-red text-weight-bold">目前勾選 {{ selectedRows.length }} 筆</span></div>
          <div class="text-grey">查詢筆數：{{ tableData.length }} 筆</div>
        </div>

        <q-table
          class="my-sticky-header-table"
          :rows="tableData"
          :columns="columns"
          row-key="sno"
          selection="multiple"
          v-model:selected="selectedRows"
          virtual-scroll
          :pagination="{ rowsPerPage: 0 }" 
          hide-pagination
          bordered
          flat
        >
          <template v-slot:body-cell-executionResult="props">
            <q-td :props="props" :class="{ 'text-fail': props.row.executionResult === '失敗' }">
              {{ props.row.executionResult }}
            </q-td>
          </template>
        </q-table>
      </q-card-section>

      <q-separator />

      <q-card-section>
        <div class="sub-title">重新設定安排方式：</div>
        
        <div class="column q-gutter-sm">
          <q-radio v-model="arrangeType" val="1" label="安排至今日約訪時間" />
          
          <div class="row items-center">
            <q-radio v-model="arrangeType" val="2" label="安排至 今日 +" />
            <q-input v-model.number="addDays" type="number" dense filled class="q-mx-sm days-counter-input" />
            <span>工作天</span>
          </div>

          <div class="row items-center">
            <q-radio v-model="arrangeType" val="3" label="安排至此約訪時間：" />
            
            <q-input filled v-model="specificDate" mask="####-##-## ##:##:##" placeholder="YYYY-MM-DD HH:mm:ss" dense class="q-ml-sm datetime-input" :disable="arrangeType !== '3'">
              <template v-slot:append>
                <q-icon name="event" class="cursor-pointer">
                  <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                    <div class="row items-center justify-center q-gutter-sm q-pa-md bg-white">
                      <q-date v-model="specificDate" mask="YYYY-MM-DD HH:mm:ss" color="positive" />
                      <q-time v-model="specificDate" mask="YYYY-MM-DD HH:mm:ss" with-seconds color="positive" format24h />
                    </div>
                  </q-popup-proxy>
                </q-icon>
              </template>
            </q-input>
            
            <span class="text-grey q-ml-md">(若選擇該項，只會處理你所勾選的第一筆資料)</span>
          </div>
        </div>

        <div class="row justify-center q-mt-lg">
          <q-btn color="positive" label="儲存" size="lg" @click="doSave" />
        </div>
      </q-card-section>

    </q-card>
  </q-page>
</template>

<script setup lang="ts">
import { onMounted } from 'vue';
import { useUpdTime } from '../composables/useUpdTime';
import '../assets/main.css';

// 引用抽離的邏輯
const { 
  startTime, endTime, searchErrorMsg, tableData, selectedRows, 
  arrangeType, addDays, specificDate, 
  initDates, doSearch, doSave 
} = useUpdTime();

// 定義表格欄位
const columns = [
  { name: 'executionResult', label: '執行結果', field: 'executionResult', align: 'center' as const },
  { name: 'listNo', label: '名單序號', field: 'listNo', align: 'center' as const },
  { name: 'custName', label: '客戶姓名', field: 'custName', align: 'center' as const },
  { name: 'campName', label: '專案名稱', field: 'campName', align: 'center' as const },
  { name: 'recallTime', label: '約訪時間', field: 'recallTime', align: 'center' as const },
  { name: 'listLastPhone', label: '撥出電話', field: 'listLastPhone', align: 'center' as const },
  { name: 'campServiceDt', label: '名單回收日', field: 'campServiceDt', align: 'center' as const }
];

// 頁面載入時初始化日期
onMounted(() => {
  initDates();
});
</script>
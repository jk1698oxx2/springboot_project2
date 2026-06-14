import { ref, computed, watch } from 'vue';
import { date, useQuasar } from 'quasar';
import { customerApi } from '../api/customerApi';

export function useUpdTime() {
  const $q = useQuasar();

  // --- 狀態變數 ---
  const startTime = ref('');
  const endTime = ref('');
  const searchErrorMsg = ref(''); // 綠色副標下方的紅字提示
  
  const tableData = ref<any[]>([]);
  const selectedRows = ref<any[]>([]);
  
  const arrangeType = ref('1'); // 1: 今日, 2: 今日+N天, 3: 指定時間
  const addDays = ref(0);
  const specificDate = ref('');
  
  // --- 1. 初始化預設日期 ---
  const initDates = () => {
    const now = new Date();
    const yesterday = date.subtractFromDate(now, { days: 1 });
    
    // 開始時間: 前一日的年月日，現行的時分秒
    startTime.value = date.formatDate(yesterday, 'YYYY-MM-DD HH:mm:ss');
    // 結束時間: 當日的年月日，22:00:00
    endTime.value = date.formatDate(now, 'YYYY-MM-DD 22:00:00');
  };

  // --- 2. 查詢邏輯與檢核 ---
  const doSearch = async () => {
    searchErrorMsg.value = '';
    tableData.value = [];
    selectedRows.value = [];

    // 檢核空值
    if (!startTime.value || !endTime.value) {
      searchErrorMsg.value = '查詢區間不得為空值，請重新輸入';
      return;
    }

    const start = new Date(startTime.value);
    const end = new Date(endTime.value);
    const now = new Date();
    
    // 檢核 60 天限制
    const diffDays = date.getDateDiff(end, start, 'days');
    const pastDiff = date.getDateDiff(now, start, 'days');
    const futureDiff = date.getDateDiff(end, now, 'days');

    if (pastDiff > 60 || futureDiff > 60 || diffDays > 60 || diffDays < 0) {
      searchErrorMsg.value = '查詢期間僅能兩個月，請重新輸入';
      return;
    }

    try {
      const res = await customerApi.queryUnfinished(startTime.value, endTime.value);
      if (res.data.returnCode === '0') {
        tableData.value = res.data.data;
        if (tableData.value.length === 0) {
          $q.notify({ type: 'warning', message: '查無資料，請再確認' });
        }
      } else {
        searchErrorMsg.value = res.data.msg;
      }
    } catch (error) {
      $q.notify({ type: 'negative', message: '系統連線異常' });
    }
  };

  // --- 3. 監聽安排方式變化 (特定邏輯) ---
  // 若選擇「安排至此約訪時間」(3)，僅保留排序第一筆的勾選狀態 
  watch(arrangeType, (newVal) => {
    if (newVal === '3' && selectedRows.value.length > 1) {
      // 依約訪日期由舊到新排序，取第一筆
      const sorted = [...selectedRows.value].sort((a, b) => 
        new Date(a.recallTime).getTime() - new Date(b.recallTime).getTime()
      );
      selectedRows.value = [sorted[0]];
      $q.notify({ type: 'info', message: '已自動保留舊到新的第一筆資料' });
    }
  });

  // --- 4. 儲存更新邏輯 ---
  const doSave = async () => {
    if (selectedRows.value.length === 0) {
      $q.notify({ type: 'warning', message: '請先勾選要更新的案件' });
      return;
    }

    // 組裝 Request 陣列 (依據 arrangeType 計算每筆的目標時間)
    const reqList = selectedRows.value.map(row => {
      let targetTime = row.recallTime;
      const originalDateObj = new Date(row.recallTime);
      const today = new Date();

      if (arrangeType.value === '1') {
         // 今日約訪: 換成今天日期，保留時分秒
         targetTime = date.formatDate(today, 'YYYY-MM-DD') + ' ' + date.formatDate(originalDateObj, 'HH:mm:ss');
      } else if (arrangeType.value === '2') {
         // 今日 + N天: 換成未來日期，保留時分秒
         const targetDate = date.addToDate(today, { days: addDays.value });
         targetTime = date.formatDate(targetDate, 'YYYY-MM-DD') + ' ' + date.formatDate(originalDateObj, 'HH:mm:ss');
      } else if (arrangeType.value === '3') {
         // 指定時間
         targetTime = specificDate.value;
      }
      
      return { sno: row.sno, recallTime: targetTime };
    });

    try {
      const payload = { arrangeType: arrangeType.value, reqList };
      const res = await customerApi.updateAppoint(payload);
      
      if (res.data.returnCode === '0') {
        const resultData = res.data.data;
        // 提示統計訊息
        $q.notify({ 
          type: resultData.failCount > 0 ? 'warning' : 'positive', 
          message: `勾選${resultData.totalCount}筆，完成${resultData.successCount}筆，失敗${resultData.failCount}筆` 
        });

        if (resultData.failCount === 0) {
          // 失敗筆數為 0：重新查詢
          doSearch();
        } else {
          // 失敗筆數 >= 1：明細保留，更新執行結果欄位
          tableData.value.forEach(row => {
            if (resultData.detailedResults[row.sno]) {
              row.executionResult = resultData.detailedResults[row.sno];
            }
          });
          selectedRows.value = []; // 清空勾選
        }
      } else {
        $q.notify({ type: 'negative', message: res.data.msg });
      }
    } catch (error) {
      $q.notify({ type: 'negative', message: '更新過程發生異常' });
    }
  };

  return {
    startTime, endTime, searchErrorMsg, tableData, selectedRows,
    arrangeType, addDays, specificDate,
    initDates, doSearch, doSave
  };
}
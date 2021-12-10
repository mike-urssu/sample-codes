# Bulk Insert

Load Data와 Batch Insert 성능 비교

## 테스트 결과

| rows | 1000 | 10000 | 50000 | 100000 | 500000 |
| ---- | ---- | ---- | ---- | ---- | ---- |
| Load Data (ms) | 65 | 60 | 92 | 166 | 311 |
| Batch Insert (ms) | 1112 | 8795 | 41426 | 81638 | 244449 |

![bulk insert performance](https://user-images.githubusercontent.com/69888508/145130271-3c9e915e-166d-4494-9f9b-386800093fe3.png)

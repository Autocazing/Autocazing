import StockTable from "./StockTable";
import StockManagementModal from "./StockManagementModal";
import { useState } from "react";
import ExcelJS from "exceljs";

import { StockGetApi } from "../../../apis/server/StockApi";
const StockManagement = () => {
    const [modalIsOpen, setModalIsOpen] = useState(false);
    const { data: stockInfo, isLoading, isError, error } = StockGetApi();

    const StockList = [
        // 테스트용
        {
            name: "우유(1L)",
            expiration: "2024-04-27",
            amount: 3,
        },
        {
            name: "우유(1L)",
            expiration: "2024-04-29",
            amount: 7,
        },
        {
            name: "우유(1L)",
            expiration: "2024-05-02",
            amount: 11,
        },
        {
            name: "원두(2KG)",
            expiration: "2024-04-27",
            amount: 8,
        },
        {
            name: "우유(1L)",
            expiration: "2024-04-27",
            amount: 2,
        },
    ];

    const excelsample = async () => {
        // 엑셀 워크북 생성
        const workbook = new ExcelJS.Workbook();

        // 워크시트 생성
        const worksheet = workbook.addWorksheet("Sample");

        // 열 너비 설정
        worksheet.getColumn(1).width = 15;
        worksheet.getColumn(2).width = 15;
        worksheet.getColumn(3).width = 15;

        // 헤더 행 추가
        const firstRow = worksheet.addRow([
            "품목명",
            "유통기한",
            "총량",
            "*추가할 재고를 예시 형식에 맞게 2행부터 작성",
        ]);

        firstRow.eachCell((cell, number) => {
            if (number <= 3) {
                cell.fill = {
                    type: "pattern",
                    pattern: "solid",
                    fgColor: { argb: "59b0fc" },
                };
            } else {
                cell.font = {
                    bold: true,
                };
            }
        });

        // 기존 재고 정보를 엑셀 행으로 추가
        StockList.forEach((stock) => {
            const newRow = worksheet.addRow([]);
            newRow.getCell(1).value = stock.name;
            newRow.getCell(2).value = stock.expiration;
            newRow.getCell(3).value = stock.amount;
        });

        // 엑셀 파일로 저장
        workbook.xlsx.writeBuffer().then((buffer) => {
            const blob = new Blob([buffer], {
                type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            });
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement("a");
            a.href = url;
            a.download = "sample.xlsx";
            a.click();
        });
    };
    return (
        <>
            <div className="mb-6 flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
                <h2 className="text-title-md2 font-semibold text-black dark:text-white">
                    재고 관리
                </h2>
                <ol className="flex items-center gap-2">
                    <li>가게 관리 /</li>
                    <li className="font-bold text-primary">재고 관리</li>
                </ol>
            </div>
            <StockTable stockInfo={stockInfo} />
            <div className="flex justify-end mt-6">
                <button
                    className="mr-3 bg-transparent hover:bg-primary text-primary font-semibold hover:text-white py-2 px-4 border border-primary hover:border-transparent rounded"
                    onClick={excelsample}
                >
                    엑셀 예제
                </button>
                <button
                    onClick={() => setModalIsOpen(true)}
                    className="bg-transparent hover:bg-primary text-primary font-semibold hover:text-white py-2 px-4 border border-primary hover:border-transparent rounded"
                >
                    재고추가
                </button>
            </div>
            {modalIsOpen && (
                <StockManagementModal
                    isOpen={modalIsOpen}
                    onClose={() => setModalIsOpen(false)}
                    initialValue={[]}
                />
            )}
        </>
    );
};

export default StockManagement;

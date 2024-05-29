import Modal from "react-modal";
import { useEffect, useState } from "react";
import closeIcon from "../../../images/icon/close.svg";
import minus from "../../../images/icon/minus.svg";
import plus from "../../../images/icon/plus.svg";
import { MaterialGetApi } from "../../../apis/server/MaterialApi";
import AWS from "aws-sdk";
import { MenuPostApi, MenuEditApi } from "../../../apis/server/MenuApi";
import Swal from "sweetalert2";

const customStyles = {
    overlay: {
        backgroundColor: "rgba(0, 0, 0, 0.4)",
        width: "100%",
        height: "100vh",
        zIndex: "10",
        position: "fixed",
        top: "0",
        left: "0",
    },
    content: {
        width: "30rem",
        height: "65%",
        zIndex: "150",
        position: "absolute",
        top: "50%",
        left: "50%",
        transform: "translate(-50%, -50%)",
        borderRadius: "10px",
        boxShadow: "2px 2px 2px rgba(0, 0, 0, 0.25)",
        backgroundColor: "white",
        padding: "20px",
        display: "flex",
        flexDirection: "column",
        overflow: "auto",
    },
};

const MenuManagementModal = ({ isOpen, onClose, initialValue }) => {
    useEffect(() => {
        AWS.config.update({
            accessKeyId: "AKIAW6NUKBVLWSUBAOXJ ",
            secretAccessKey: "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",
        });

        const myBucket = new AWS.S3({
            params: { Bucket: "e204" },
            region: "us-east-1",
        });
    }, []);
    const [menuPostData, setMenuPostData] = useState({
        // storeId: 1,
        menuName: initialValue.menuName || "",
        menuPrice: initialValue.menuPrice || 0,
        onEvent: initialValue.onEvent || false,
        ingredients: initialValue.ingredientoDtoList || [
            { ingredientId: 0, capacity: 0 },
        ],

        discountRate: initialValue.discountRate || 0,
        imageUrl: initialValue.imageUrl || "",
        soldOut: false,
    });

    const { data: materialInfo, isLoading, isError, error } = MaterialGetApi();
    const postMenu = MenuPostApi();
    const editMenu = MenuEditApi(initialValue.menuId);

    const uploadFileToS3 = async (file) => {
        const s3 = new AWS.S3({
            accessKeyId: "AKIAW6NUKBVLWSUBAOXJ ", // 실제 사용하실 때 여기에 AWS Access Key를 입력하세요
            secretAccessKey: "jihVSFQLVPdtMxeMRCBSPVBfxf1kOU1jsUUy3p60", // 실제 사용하실 때 여기에 AWS Secret Key를 입력하세요
            region: "us-east-1",
        });

        const params = {
            Bucket: "e204", // 버킷 이름
            Key: `upload/${file.name}`, // 파일명
            Body: file,
        };

        try {
            const { Location } = await s3.upload(params).promise();
            return Location; // 업로드된 파일의 URL 반환
        } catch (err) {
            console.error("S3 Upload Error: ", err);
            return ""; // 에러 발생 시 빈 문자열 반환
        }
    };
    const handleInputChange = async (e) => {
        const { name, value, type, checked, files } = e.target;

        if (type === "file" && files.length > 0) {
            // 파일을 S3에 업로드하고 URL을 받아서 상태를 업데이트합니다.
            const uploadedUrl = await uploadFileToS3(files[0]);
            setMenuPostData((prev) => ({
                ...prev,
                imageUrl: uploadedUrl, // 파일 업로드 후 받은 URL로 업데이트
            }));
        } else if (type === "checkbox") {
            setMenuPostData((prev) => ({
                ...prev,
                [name]: checked,
            }));
        } else if (name.includes("-")) {
            const [field, index] = name.split("-");
            const idx = parseInt(index, 10);
            const updatedIngredients = menuPostData.ingredients.map(
                (item, i) => {
                    return i === idx
                        ? {
                              ...item,
                              [field]:
                                  type === "number"
                                      ? parseInt(value, 10)
                                      : value,
                          }
                        : item;
                },
            );
            setMenuPostData((prev) => ({
                ...prev,
                ingredients: updatedIngredients,
            }));
        } else {
            setMenuPostData((prev) => ({
                ...prev,
                [name]: type === "number" ? parseInt(value, 10) : value,
            }));
        }
    };

    const addIngredient = () => {
        setMenuPostData((prev) => ({
            ...prev,
            ingredients: [
                ...prev.ingredients,
                { ingredientId: 0, capacity: 0 },
            ],
        }));
    };

    const removeIngredient = (index) => {
        const newIngredients = menuPostData.ingredients.filter(
            (_, i) => i !== index,
        );
        setMenuPostData((prev) => ({
            ...prev,
            ingredients: newIngredients,
        }));
    };

    const handleSubmit = (e) => {
        postMenu.mutate(menuPostData, {
            onSuccess: () => {
                Swal.fire({
                    title: "메뉴 추가 완료!",
                    text: "메뉴 정보가 성공적으로 추가되었습니다.",
                    icon: "success",
                    iconColor: "#3C50E0",
                    confirmButtonText: "확인",
                    confirmButtonColor: "#3C50E0",
                }).then((result) => {
                    if (result.isConfirmed) {
                        onClose(); // 모달 닫기
                    }
                });
            },
            onError: (error) => {
                Swal.fire({
                    title: "추가 실패",
                    text: `메뉴 추가 중 오류 발생: ${error.message}`,
                    icon: "error",
                    confirmButtonText: "확인",
                });
            },
        });
    };

    const handleEdit = (e) => {
        editMenu.mutate(menuPostData, {
            onSuccess: () => {
                Swal.fire({
                    title: "메뉴 수정 완료!",
                    text: "메뉴 정보가 성공적으로 수정되었습니다.",
                    icon: "success",
                    iconColor: "#3C50E0",
                    confirmButtonText: "확인",
                    confirmButtonColor: "#3C50E0",
                }).then((result) => {
                    if (result.isConfirmed) {
                        onClose(); // 모달 닫기
                    }
                });
            },
            onError: (error) => {
                Swal.fire({
                    title: "수정 실패",
                    text: `메뉴 수정 중 오류 발생: ${error.message}`,
                    icon: "error",
                    confirmButtonText: "확인",
                });
            },
        });
    };

    if (isLoading) return <div>Loading...</div>;
    if (isError) return <div>Error: {error.message}</div>;

    return (
        <Modal
            isOpen={isOpen}
            onRequestClose={onClose}
            style={customStyles}
            contentLabel="Menu Management"
            ariaHideApp={false}
        >
            <div className="flex justify-end">
                <button
                    onClick={onClose}
                    style={{
                        background: "transparent",
                        border: "none",
                        cursor: "pointer",
                    }}
                    aria-label="Close modal"
                >
                    <img
                        src={closeIcon}
                        alt="Close"
                        style={{ width: "24px", height: "24px" }}
                    />
                </button>
            </div>
            {Object.keys(initialValue).length === 0 ? (
                <h1 className="text-center text-3xl my-4 font-semibold text-black dark:text-white">
                    메뉴 추가
                </h1>
            ) : (
                <h1 className="text-center text-3xl my-4 font-semibold text-black dark:text-white">
                    메뉴 수정
                </h1>
            )}
            <div className="p-6.5">
                <div className="mb-4.5">
                    <div className="flex justify-center items-center mb-3">
                        <div className="w-36 h-36 rounded-full overflow-hidden bg-gray-300 flex items-center justify-center text-lg">
                            {menuPostData.imageUrl ? (
                                <img
                                    src={menuPostData.imageUrl}
                                    alt="Preview"
                                    style={{
                                        width: "100%",
                                        height: "100%",
                                        objectFit: "cover",
                                    }}
                                />
                            ) : (
                                "메뉴사진"
                            )}
                        </div>
                    </div>
                    <label className="mb-2.5 block text-black dark:text-white">
                        메뉴사진
                    </label>
                    <input
                        name="imageUrl"
                        onChange={handleInputChange}
                        type="file"
                        className="w-full cursor-pointer rounded-lg border-[1.5px] border-stroke bg-transparent outline-none transition file:mr-5 file:border-collapse file:cursor-pointer file:border-0 file:border-r file:border-solid file:border-stroke file:bg-whiter file:py-3 file:px-5 file:hover:bg-primary file:hover:bg-opacity-10 focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:file:border-form-strokedark dark:file:bg-white/30 dark:file:text-white dark:focus:border-primary"
                    />
                </div>
                <div className="mb-4.5">
                    <label className="mb-2.5 block text-black dark:text-white">
                        할인여부
                    </label>
                    <input
                        value={menuPostData.onEvent}
                        type="checkbox"
                        name="onEvent"
                        checked={menuPostData.onEvent}
                        onChange={handleInputChange}
                        className="w-6 h-6 rounded border-stroke bg-transparent text-black focus:border-primary"
                    />
                </div>
                {menuPostData.onEvent && (
                    <div className="mb-4.5">
                        <label className="mb-2.5 block text-black dark:text-white">
                            할인율 (%)
                        </label>
                        <input
                            type="number"
                            min={0}
                            name="discountRate"
                            placeholder="할인율 입력"
                            value={menuPostData.discountRate}
                            onChange={handleInputChange}
                            className="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 text-black outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary"
                        />
                    </div>
                )}
                <div className="mb-4.5">
                    <label className="mb-2.5 block text-black dark:text-white">
                        메뉴이름
                    </label>
                    <input
                        name="menuName"
                        value={menuPostData.menuName}
                        onChange={handleInputChange}
                        type="text"
                        placeholder="메뉴 이름 입력"
                        className="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 text-black outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary"
                    />
                </div>
                <div className="mb-4.5">
                    <label className="mb-2.5 block text-black dark:text-white">
                        메뉴가격
                    </label>
                    <input
                        type="number"
                        min={0}
                        name="menuPrice"
                        placeholder="메뉴 가격 입력"
                        value={menuPostData.menuPrice}
                        onChange={handleInputChange}
                        className="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 text-black outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary"
                    />
                </div>

                {menuPostData.ingredients.map((ingredient, index) => (
                    <div
                        key={index}
                        className="mb-4.5 flex items-center justify-between"
                    >
                        <div className="flex-1 mr-2">
                            <label className="block text-black dark:text-white mb-2">
                                재료명
                            </label>
                            <select
                                name={`ingredientId-${index}`}
                                data-index={index}
                                value={ingredient.ingredientId}
                                onChange={handleInputChange}
                                className="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 text-black outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary"
                            >
                                <option value="">재료 선택</option>
                                {materialInfo.map((material) => (
                                    <option
                                        key={material.ingredientId}
                                        value={material.ingredientId}
                                    >
                                        {material.ingredientName}
                                    </option>
                                ))}
                            </select>
                        </div>
                        <div className="flex-1 mr-2">
                            <label className="block text-black dark:text-white mb-2">
                                재료량
                            </label>
                            <input
                                type="number"
                                min={0}
                                name={`capacity-${index}`}
                                data-index={index}
                                value={ingredient.capacity}
                                onChange={handleInputChange}
                                placeholder="재료량 입력"
                                className="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 text-black outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary"
                            />
                        </div>
                        <button
                            onClick={() => removeIngredient(index)}
                            className="mt-8 text-white  rounded"
                        >
                            <img
                                src={minus}
                                alt="Remove"
                                style={{ width: "25px", height: "25px" }}
                            />
                        </button>
                    </div>
                ))}

                <div className="flex justify-center">
                    <button
                        onClick={addIngredient}
                        className="mt-1 flex justify-center rounded p-3 font-medium text-gray hover:bg-opacity-90 w-12"
                    >
                        <img
                            src={plus}
                            alt="Add"
                            style={{ width: "30px", height: "30px" }}
                        />
                    </button>
                </div>
            </div>
            {Object.keys(initialValue).length === 0 ? (
                <button
                    onClick={handleSubmit}
                    className="flex w-full justify-center rounded bg-primary p-3 font-medium text-gray hover:bg-opacity-90 "
                >
                    추가하기
                </button>
            ) : (
                <button
                    onClick={handleEdit}
                    className="flex w-full justify-center rounded bg-primary p-3 font-medium text-gray hover:bg-opacity-90 "
                >
                    수정하기
                </button>
            )}
        </Modal>
    );
};

export default MenuManagementModal;

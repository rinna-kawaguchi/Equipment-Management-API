import { Divider, HStack, Heading, Input } from "@chakra-ui/react"
import { useNavigate, useParams } from "react-router-dom";
import { BaseButton } from "./atoms/BaseButton";
import { ChangeEvent, useEffect, useState } from "react";
import { Equipment } from "./FindEquipment";
import axios from "axios";

export const UpdateEquipment = () => {
  const [updateName, setUpdateName] = useState("");
  const [updateNumber, setUpdateNumber] = useState("");
  const [updateLocation, setUpdateLocation] = useState("");
  const [updateEquipment, setUpdateEquipment] = useState<Equipment | null>(null);
  const [updateMessage, setUpdateMessage] = useState("")

  const { id } = useParams();

  useEffect(() => {
    axios.get<Equipment>(`http://localhost:8080/equipments/${id}`).then((res) => setUpdateEquipment(res.data))
  }, [])

  useEffect(() => {
    setUpdateName(updateEquipment?.name ?? "");
    setUpdateNumber(updateEquipment?.number ?? "");
    setUpdateLocation(updateEquipment?.location ?? "");
  }, [updateEquipment]);

  const onChangeUpdateName = (e: ChangeEvent<HTMLInputElement>) => setUpdateName(e.target.value)
  const onChangeUpdateNumber = (e: ChangeEvent<HTMLInputElement>) => setUpdateNumber(e.target.value)
  const onChangeUpdateLocation = (e: ChangeEvent<HTMLInputElement>) => setUpdateLocation(e.target.value)

  const navigate = useNavigate();

  const onClickBackDetailPage = () => navigate(`/detail/${id}`)
  const onClickUpdate = () => {
    alert("更新しますか？")
    axios.patch(`http://localhost:8080/equipments/${id}`,
    { "name": updateName, "number": updateNumber, "location": updateLocation })
    .then((res) => setUpdateMessage(res.data.message));
    alert(updateMessage);
    navigate(`/detail/${id}`)}

  return (
    <div>
      <Heading>設備情報修正</Heading>
      <br />
      <Heading size={"md"}>設備情報詳細</Heading>
      <Divider my={3} />
      <HStack>
        <p>設備名称</p>
        <Input value={updateName} width={"400px"} placeholder="設備名称" onChange={onChangeUpdateName} isReadOnly={false} />
        <p>設備番号</p>
        <Input value={updateNumber} width={"400px"} placeholder="設備番号" onChange={onChangeUpdateNumber} />
        <p>設置場所</p>
        <Input value={updateLocation} width={"400px"} placeholder="設置場所" onChange={onChangeUpdateLocation} />
      </HStack>
      <br />
      <br />
      <BaseButton onClick={onClickBackDetailPage}>戻る</BaseButton>
      <BaseButton onClick={onClickUpdate}>更新</BaseButton>
    </div>
  )
}
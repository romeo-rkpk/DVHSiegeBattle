package com.danvhae.minecraft.siege.battle.enums

/**
 * 화살에 피격되었을 때 데미지 양을 계산하는 방법
 */
enum class ArrowDamageModel {
    /**
     * 제어 안 함
     */
    NOT_CONTROL,

    /**
     * 운동에너지에 의한 손상 모델. 속력의 제곱에 비례하여 데미지를 재계산한다
     */
    KINETIC_ENERGY,

    /**
     * 상한이 있는 운동에너지 모델. 속력의 제곱에 비례하여 데미지를 계산하되 그 높여주지 않는다
     */
    KINETIC_ENERGY_LIMITED,



    /**
     * 운동량에 의한 손상 모델, 속력에 비례하여 데미지를 재계산한다.
     */
    MOMENTUM,

    /**
     * 상한이 있는 운동량 메돌. 속력에 비례하여 데미지를 계산하되 높여주지는 않는다.
     */
    MOMENTUM_LIMITED
}
package com.fiberhome.mapps.vote.entity;

import javax.persistence.*;

@Table(name = "vt_vote_item")
public class VtVoteItem {
    /**
     * 投票项id
     */
    @Id
    private String id;

    /**
     * 所属投票id
     */
    @Column(name = "vote_info_id")
    private String voteInfoId;

    /**
     * 投票项内容描述
     */
    private String content;

    /**
     * 投票项图片id
     */
    private String image;

    /**
     * 选项顺序
     */
    @Column(name = "item_order")
    private Long itemOrder;

    /**
     * 获取投票项id
     *
     * @return id - 投票项id
     */
    public String getId() {
        return id;
    }

    /**
     * 设置投票项id
     *
     * @param id 投票项id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取所属投票id
     *
     * @return vote_info_id - 所属投票id
     */
    public String getVoteInfoId() {
        return voteInfoId;
    }

    /**
     * 设置所属投票id
     *
     * @param voteInfoId 所属投票id
     */
    public void setVoteInfoId(String voteInfoId) {
        this.voteInfoId = voteInfoId;
    }

    /**
     * 获取投票项内容描述
     *
     * @return content - 投票项内容描述
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置投票项内容描述
     *
     * @param content 投票项内容描述
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取投票项图片id
     *
     * @return image - 投票项图片id
     */
    public String getImage() {
        return image;
    }

    /**
     * 设置投票项图片id
     *
     * @param image 投票项图片id
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * 获取选项顺序
     *
     * @return item_order - 选项顺序
     */
    public Long getItemOrder() {
        return itemOrder;
    }

    /**
     * 设置选项顺序
     *
     * @param itemOrder 选项顺序
     */
    public void setItemOrder(Long itemOrder) {
        this.itemOrder = itemOrder;
    }
}